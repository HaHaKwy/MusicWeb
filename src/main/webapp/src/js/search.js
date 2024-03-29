// ===============搜索功能========================
// searchPage顶部菜单input回车搜索
$("#inpSearch").on("keydown", function (ev) {
    var ev = ev || window.event;
    if (ev.keyCode === 13) {
        funcSearch(3);
    }
});
// searchPage顶部菜单query图标点击搜索
$("#top_searchBtn").on("click", function () {
    funcSearch(3);
});

//首页  获取搜索关键字，跳转到搜素页面
function toSearchPage() {
    var strSearch = $("#inpSearch_main").val().trim();
    localStorage.setItem('flag', '1');
    localStorage.setItem('key', strSearch);
    location.href = './searchPage.html';
}

// 搜索歌曲功能函数
function funcSearch(type) {
    var strSearch = $("#inpSearch").val().trim();
    console.log(type);
    var url=""
	if (!strSearch) {
	}
    else {
        if (type == 0) {
            searchRequest("http://"+IP_ADDR+":8080/MusicPlayer/music/search?songname="+strSearch, strSearch);
        }
        else if (type == 1) {
            serarchSinger('http://'+IP_ADDR+':8080/MusicPlayer/singer/searchSinger?singer='+strSearch,strSearch);
        }
        else if (document.getElementById("song").checked) {
            searchRequest("http://"+IP_ADDR+":8080/MusicPlayer/music/search?songname="+strSearch, strSearch);
        }
        else if (document.getElementById("singer").checked) {
            serarchSinger('http://'+IP_ADDR+':8080/MusicPlayer/singer/searchSinger?singer='+strSearch,strSearch);
        }
    }
};

// 搜索请求数据
//songName singer album length
function searchRequest(url,str) {
    axios.get(url)
        .then(response => {
            if (response.data.status == 0){
                messageBox('什么都没有呢...','error');
            }
            else {
                messageBox('搜索成功！','notice');
                let result = response.data.content;
                var listHtml = '';
                for (var i = 0; i < result.length; i++) {
                    var minute = '0'+Math.trunc(result[i]['length']/60);
                    var second = (result[i]['length']%60 < 10) ? '0'+result[i]['length']%60:result[i]['length']%60;
                    listHtml += `
                    			<tr>
                    				<td class="index" data-num="`+ ((+i + 1) < 10 ? "0" + (+i + 1) : (+i + 1)) + `">` + ((+i + 1) < 10 ? "0" + (+i + 1) : (+i + 1)) + `</td>
                    				<td><i class="fa fa-heart-o" aria-hidden="true"></i>&nbsp;<i class="fa fa-download" aria-hidden="true"></i></td>
                    				<td>`+ result[i]['songname'] + `</td>
                    				<td>`+ result[i]['singer'] + `</td>
                    				<td>`+ result[i]['album'] + `</td>
                    				<td>`+ minute+':'+second + `</td>
                    			</tr>
                    		`;
                }
                var spans = $("#search_count span");
                spans[0].innerText = str;
                spans[1].innerText = result.length;
                $("#infoList_search").html(listHtml);
                sessionStorage.setItem("searchString", str);
            }
        })
        .catch(function (error) {
            console.log(error);
            messageBox('未知异常，请联系开发人员 @_@ ','error');
        });
}

function serarchSinger(url,str){
    axios.get(url)
        .then(response => {
            if (response.data.status == 0){
                messageBox('什么都没有呢...','error');
            }
            else {
                messageBox('搜索成功！','notice');
                let result = response.data.content;
                var listHtml = '';
                for (var i = 0; i < result.length; i++) {
                    listHtml += `
                                <div>
                                    <img src="`+ result[i]['imagePath'] + `" alt="歌手图片">
                                    <p id="singername">`+ result[i]['singername'] + `</p>
                                    <p id="singerinfo">
                                        专辑数:<span>`+ result[i]['albumnum'] + `</span>&nbsp;单曲数:<span>` + result[i]['songnum'] + `</span>
                                    </p>
                                </div>
                                `;
                }
                var spans = $("#search_count span");
                spans[0].innerText = str;
                spans[1].innerText = result.length;
                console.log(listHtml);
                $("#singerList_search").html(listHtml);
                sessionStorage.setItem("searchString", str);
            }
        })
        .catch(function (error) {
            console.log(error);
            messageBox('未知异常，请联系开发人员 @_@ ','error');
        });
}

$infoList_search = $("#infoList_search");// 搜索列表
// 双击搜索列表tr播放
$infoList_search.on("dblclick", "tr", function () {
    var ele=$(this).find("td");
    var key=ele.eq(2).text()+'_'+ele.eq(3).text();
//    SearchMusic(key);
    localStorage.setItem("songname",ele.eq(2).text());
    localStorage.setItem("singer",ele.eq(3).text())
    localStorage.setItem("albumName","");
    localStorage.setItem("albumAuthor","");
    location.href="./player.html";
    console.log(key);
});

// 单击收藏
$infoList_search.on("click", ".fa-heart-o", function () {
    var songname=$(this).parent().next();
    var singer=songname.next();
    console.log(songname.text()+'_'+singer.text());

    localStorage.setItem("songname",songname.text());
    localStorage.setItem("singername",singer.text());

    var form_1=document.getElementsByClassName("form_1");
    searchList();
    if(localStorage.getItem("userState") == "login"){
        form_1[0].className="form_1 open";
    }
});

function searchList(){
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/user/allLikelist")
    .then(response => {
        console.log("search");
        if(response.data.status == -1){
            messageBox('请先登陆再来哦','error');
        }
        else{
            let result = response.data.content;
            var listHtml = '';
            for (var i = 0; i < response.data.status; i++) {
                listHtml += `
                            <label>
                                <input type="checkbox" class="box" name="parent_checkbox" />`
                                +result[i]+
                            `</label>
                            `;
            }
            $(".group").html(listHtml);
        }
    })
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}

// 单击下载
$infoList_search.on("click", ".fa-download", function () {
    var songname=$(this).parent().next();
    var singer=songname.next();
    $("#search_dld").attr('href',"http://"+IP_ADDR+":8080/MusicPlayer/music/downloadSong?songname="+songname.text().trim()+"&singer="+singer.text().trim());
    $("#search_dld")[0].click();
});

// searchPage顶部菜单query图标点击搜索
$("#singerList_search").on("click", "img", function () {
    var ele=$(this).next();
    localStorage.setItem('key2', ele.text());
    localStorage.setItem('flag', "2");
    location.href='./detailPage.html';
    // serarchSingerDetail(ele.text());
});

$("#songTab").on("click",function(){console.log("songclick");funcSearch(0)});
$("#singerTab").on("click",function(){console.log("singerclick");funcSearch(1)});

// init
$(function () {

	if(localStorage.getItem('flag')=='1'){
		var str=localStorage.getItem('key');
		console.log(str);
		$('#inpSearch').val(str);
		console.log($("#inpSearch").val());
		localStorage.setItem('flag','0');
        $("#songTab").click();
	}
	if(localStorage.getItem('flag')=='2'){
    		var str=localStorage.getItem('key');
    		console.log(str);
    		$('#inpSearch').val(str);
    		console.log($("#inpSearch").val());
    		localStorage.setItem('flag','0');
            $("#singerTab").click();
    	}
});


