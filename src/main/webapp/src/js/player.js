var tmArr=new Array();
// for(var i=0;i<50;++i)
//     tmArr[i]=i+1;
var htArr=new Array();
// for(var i=0;i<50;++i)
//     htArr[i]=17;
var idx=0;
var sum=0;
var length=0;
var playLock=0;
var scrollLock=0;
var rotateVal = 0 // 旋转角度
var InterVal;
var isPlaying=0;

var containerHeight = $("#lrcContainer")[0].clientHeight // 容器高度
var liHeight = $("#lrcBox")[0].children[0].clientHeight // li高度
var maxHeight = $("#lrcBox")[0].clientHeight - containerHeight // 最大高度
var marginHeight = $("#lrcBox li").eq(0).css("margin-top").replace('px', ''); // margin高度
var barWidth = $("#progress_box")[0].clientWidth; // 进度条长度
var volumeWidth = $("#vol_progress_box")[0].clientWidth; // 音量条长度

window.onload=function(){
     var songname=localStorage.getItem("songname");
     var singer=localStorage.getItem("singer");
     $("#playing_sn").text(songname);
     $("#playing_sg").text(singer);

    GetInfo(songname,singer);
    GetLiric(songname,singer);
    init();
}

// 专辑名，图片
function GetInfo(songname,singer){
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/play?songname="+songname+"&singer="+singer)
    .then(response => {
        let result = response.data.content;
        $("audio")[0].src = result['path'];
        // TODO
        $("#playing_pst")[0].src = result['imagePath'];
        $("#playing_sn").text(result['songname']);
        $("#playing_sg").text(result['singer']);
        localStorage.setItem("songname",result['songname']);
        localStorage.setItem("singer",result['singer']);
        $("#playing_alb").text(result['album']);
        length=result['length'];
        var min=parseInt(length/60);
        var sec=parseInt(length%60);
        if(sec<10)
            $("#audio_duration").text(min+":0"+sec);
        else
            $("#audio_duration").text(min+":"+sec);
    })
    .catch(function (error) {
        console.log(error);
        alert("搜索失败。");
    });
}
//Taylor Swift - You Belong With Me
function GetLiric(songname,singer){
    var listHtml = '';
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/lyric?songname="+songname+"&singer="+singer)
    .then(response => {
        let result = response.data.content;
        if (response.data.status == -1)
            idx = -1;
        else {
            for (var i = 0; i < response.data.status; i++) {
                tmArr[i] = result[i]['time'];
                var lst=result[i]['lrc'].split('#');
                if(lst.length>1){
                    listHtml += `<li>
                        <p class="lrc_english">` + lst[0] +`</p>` +
                        `<p class="lrc_chinese">` + lst[1] + `</p>`+
                    `</li>`;
                }
                else{
                    listHtml += `<li>
                        <p class="lrc_english">` + result[i]['lrc'] +`</p>` +
                `   </li>`;
                }            }
            $("#lrcBox").html(listHtml);
            for(var i=0;i<tmArr.length;i++){
                htArr[i]=$("#lrcBox")[0].children[i].clientHeight;
            }
        }
    })
    .catch(function (error) {
        console.log(error);
        alert("搜索失败。");
    });
}

// 初始化
function init(){
    dealVolume(0.5);
}

function updata(){
    if(idx==-1)
        return;
    if(playLock)
        return;
    if(idx==0)
        $("#lrcBox li").eq(0).addClass("current_lrc");
    var audio = $("audio");
    var time=parseFloat(audio[0].currentTime.toFixed(2));//获取播放进度
    var secs=parseFloat(audio[0].currentTime.toFixed(0));
    processBar(secs);
    if(idx+1<tmArr.length){
        if(time>tmArr[idx+1]){
            $("#lrcBox li").eq(idx).removeClass("current_lrc");
            sum=sum+htArr[idx];
            ++idx;
            $("#lrcBox li").eq(idx).addClass("current_lrc");
            scroll();
        }
    }
}

// 歌词滚动
function scroll() {
    if(scrollLock)
        return;
    let offset = sum + marginHeight*(idx +1) - containerHeight / 2
    if (offset < 0) { //如果当前还没有开始播放
        offset = 0
    }
    if (idx > tmArr.length) { // 当前歌词位置在最后一句
        offset = 0;
    }
    $("#lrcContainer").scrollTop(offset);
    // $("#lrcBox")[0].style.transform = `translateY(-${offset}px)`
}

// 点击播放
$("#playBtnGroup").on("click", ".fa-play", function () {
    $("audio")[0].play();
    $(this)[0].style.display="none";
    $(".fa-pause")[0].style.display="block";
    isPlaying=1;
    // $(".current_lrc")[0].scrollIntoView()
    rotate();
    ndRotate();
});

// 点击暂停
$("#playBtnGroup").on("click", ".fa-pause", function () {
    $("audio")[0].pause();
    $(this)[0].style.display="none";
    isPlaying=0;
    $(".fa-play")[0].style.display="block";
    clearInterval(InterVal);
    ndRotateback();
});

// 进度条移动
function processBar(secs){
    var min=parseInt(secs/60);
    var sec=parseInt(secs%60);
    if(sec<10)
        $("#audio_currentTime").text(min+":0"+sec);
    else
        $("#audio_currentTime").text(min+":"+sec);
    $("#progress_cache")[0].style.width=barWidth*(secs/length)+'px';
    $("#progress_arc")[0].style.left=barWidth*(secs/length)+'px';
}

// 点击下一首-1
$("#playBtnGroup").on("click", ".fa-step-forward", function () {
    reset();
    switchSong(1);
});

// 点击上一首-0
$("#playBtnGroup").on("click", ".fa-step-backward", function () {
    reset();
    switchSong(0);
});

function reset(){
    isPlaying=0;
    $("audio")[0].pause();
    var img = document.getElementById('playing_pst');
    rotateVal=0;
    clearInterval(InterVal);
    ndRotateback();
    img.style.transform = 'rotate(-' + rotateVal + 'deg)'
    $(".fa-pause")[0].style.display="none";
    $(".fa-play")[0].style.display="block";
    processBar(0);
    idx=0;
    sum=0;
    rotateVal=0;
}

// 切歌
function switchSong(kind){
    var listname=localStorage.getItem("albumName");
//    console.log(listname);
    var songname=localStorage.getItem("songname");
    var singer=localStorage.getItem("singer");
    var usr=localStorage.getItem('albumAuthor');
    var k=kind;
    if(listname=="")
        k=2;
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/switchSong?songname="+songname+"&singer="+singer+"&listname="+listname+"&creator="+usr+"&flag="+k)
    .then(response => {
        if(response.data.status == -1){
            messageBox('无更多歌曲~','error');
            return;
        }
        tmArr = new Array();
        htArr = new Array();
        let result = response.data.content;
        GetInfo(result['songname'],result['singer']);
        GetLiric(result['songname'],result['singer']);
        init();
    })
    .catch(function (error) {
        console.log(error);
    });
}

// 静音
$("#muteBtn").on("click", ".fa-volume-up", function () {
    dealVolume(0);
    $(".fa-volume-up")[0].style.display="none";
    $(".fa-volume-off")[0].style.display="block";
});

// 音量恢复
$("#muteBtn").on("click", ".fa-volume-off", function () {
    dealVolume(0.5);
    $(".fa-volume-off")[0].style.display="none";
    $(".fa-volume-up")[0].style.display="block";
});

function dealVolume(v){
    $("audio")[0].volume=v;
    $("#volume_cache")[0].style.width=volumeWidth*v+'px';
    $("#vol_progress_arc")[0].style.left=volumeWidth*v+'px';
}

// 音量控制
$("#vol_progress_arc").on("mousedown", function (){
    var isMove = true;
    var offx=$("#vol_progress_box").offset().left;
    $(document).mousemove(function (e) {
        if(isMove){
            var off=e.pageX-offx;
            var v=0;
            if(off>=volumeWidth)
                v=1;
            else if(off<=0)
                v=0;
            else
                v=off/volumeWidth;
            dealVolume(v);
        }
    }).mouseup(
        function () {
        isMove = false;
    });
});

// 播放控制
$("#progress_arc").on("mousedown", function (){
    var isMove = true;
    var offx=$("#progress_box").offset().left;
    var v=0;
    var tmstamp=parseFloat($("audio")[0].currentTime.toFixed(2));
    $(document).mousemove(function (e) {
        if(isMove){
            playLock=1;
            $("audio")[0].pause();
            var off=e.pageX-offx;
            if(off>=barWidth)
                v=1;
            else if(off<=0)
                v=0;
            else
                v=off/barWidth;
            processBar(v*length);
            $("audio")[0].currentTime=v*length;
        }
    }).mouseup(
        function () {
        isMove = false;
        relocateIrc(tmstamp);
        playLock=0;
        if(isPlaying)
            $("audio")[0].play();
    });
});

// 重新定位歌词
function relocateIrc(tmstamp){
    $("#lrcBox li").eq(idx).removeClass("current_lrc");
    var curtm=parseFloat($("audio")[0].currentTime.toFixed(2));
    if(tmstamp<curtm){
        while(idx+1<tmArr.length && tmArr[idx]<curtm){
            if(tmArr[idx+1]>=curtm)
                break;
            sum=sum+htArr[idx];
            idx++;
        }
    }
    else{
        while(idx>0 && tmArr[idx]>curtm){
            idx--;
            sum=sum-htArr[idx];
        }
    }
    $("#lrcBox li").eq(idx).addClass("current_lrc");
    scroll();
}

// 手动进度条
$("#lrcContainer")[0].onscroll=function(){
    scrollLock=1;
    setTimeout(function(){scrollLock=0;},4000);
}

// 双击歌词
$("#lrcBox").on("dblclick", "li", function () {
    playLock=1;
    var audio=$("audio")[0];
    $("#lrcBox li").eq(idx).removeClass("current_lrc");
    var index=$(this).index();
    if(index>idx){
        while(idx+1<tmArr.length && index>idx){
            sum=sum+htArr[idx];
            idx++;
        }
    }
    else if(index<idx){
        while(idx>0 && index<idx){
            idx--;
            sum=sum-htArr[idx];
        }
    }
    audio.currentTime=tmArr[idx];
    $("#lrcBox li").eq(idx).addClass("current_lrc");
    var secs=parseFloat(audio.currentTime.toFixed(0));
    processBar(secs);
    scroll();
    playLock=0;
});

// 图片旋转
function rotate () {
    InterVal = setInterval(function () {
        var img = document.getElementById('playing_pst')
        rotateVal += 1
        // 设置旋转属性(顺时针)
        img.style.transform = 'rotate(' + rotateVal + 'deg)'
        // 设置旋转属性(逆时针)
        //img.style.transform = 'rotate(-' + rotateVal + 'deg)'
        // 设置旋转时的动画  匀速0.1s
        img.style.transition = '0.1s linear'
    }, 100)
}

// 针旋转
function ndRotate(){
    var offx=$("#needleImg")[0].offsetLeft;
    var img = document.getElementById('needleImg');
    img.style["transform-origin"] = "25px 5px";
    img.style.transform = 'rotate(' + 20 + 'deg)';
    img.style.transition = '0.5s linear';
}

// 针恢复
function ndRotateback(){
    var img = document.getElementById('needleImg');
    img.style["transform-origin"] = "25px 5px";
    img.style.transform = 'rotate(-' + 20 + 'deg)';
    img.style.transition = '0.5s linear';
}

