/**
  * Theme: music player data request
  * Dependence: jQuery
  *
  */
function turnToImg(id){
	$('#topShow').css('background-image','url(./src/images/poster'+String(id)+'.png)');
	$('#bottomShow').css('background-image','url(./src/images/poster'+String(id)+'.png)');
	let id_before=Number(sessionStorage.getItem('slideID'));
	sessionStorage.setItem('slideID',String(id));
	$('#slide-icon'+String(id)).css('color','rgba(255, 0, 0, 0.71)');
	$('#slide-icon'+String(id_before)).css('color','rgb(192, 192, 192)');
}
function preSlide(){
	let id=Number(sessionStorage.getItem('slideID'));
	id=(id+6)%8+1;
	turnToImg(id);
}
function nextSlide(){
	let id=Number(sessionStorage.getItem('slideID'));
	id=id%8+1;
	turnToImg(id);
}
let timer=setInterval(function(){nextSlide()},5000);
function toMyMusic(){
	let state=localStorage.getItem('userState');
	if(state=='nologin'){
		messageBox('请先登陆','error');
	}
	else{
		let userName = localStorage.getItem('userName');
		localStorage.setItem('albumName', '我喜欢的音乐');
		localStorage.setItem('albumAuthor', userName);
		window.location.href='./myMusic.html';
		//TODO 跳转前写好默认专辑
	}
}
function toRecommend(id){
	let albumName = $(".hot-left-video").children().eq(id-1).children().eq(1).text();
	localStorage.setItem('albumName', String(albumName));
	localStorage.setItem('albumAuthor', 'SYSTEM');
	window.location.href='./recommend.html';
}
function toLogin(){
	let userState=localStorage.getItem('userState');
	if(userState=='nologin'){
		window.location.href='./signup.html';
	}
	else{
		axios.post('http://'+IP_ADDR+':8080/MusicPlayer/user/exit', {
		"userState": "exit"
		}).then(response => {
			window.location.href='./index.html'
		})
	}
}
// 搜索跳转与传参
	function toSearchPage(){
		var strSearch = $("#inpSearch_main").val();
		$.trim(strSearch);
		if(strSearch != "" && strSearch != null && strSearch != undefined){
			localStorage.setItem('flag', '1');
			localStorage.setItem('key', strSearch);
		}
		location.href='./searchPage.html';
	}
// init
$(function () {
	// ===============初始化========================
	sessionStorage.setItem('slideID','1');
	localStorage.setItem('flag','0');
	// 请求登陆状态
	
	axios.get("http://"+IP_ADDR+":8080/MusicPlayer/user/userInfo")
    .then(response => {        
        //response.data.status //-1 未登录   0 已登陆
        //response.data.content.username
		if(response.data.status==-1){
			localStorage.setItem('userState','nologin');
			localStorage.setItem('userName','null');
			$('#uName').text('');
			$('#btn_login').text('登录');
		}
		else{
			localStorage.setItem('userState','login');
			localStorage.setItem('userName',response.data.content.username);
			$('#uName').text(response.data.content.username+' ');
			$('#btn_login').text('退出');
		}
    });

	axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/topSong")
    .then(response => {
		var musicList = response.data.content; //一个歌曲对象类型的数组
		let ranklist=$(".rank");
		for(i=0; i<10; ++i){
			ranklist.children().eq(i).children().eq(1).text(musicList[i]['songname']);
			ranklist.children().eq(i).children().eq(2).text(musicList[i]['singer']);
			ranklist.children().eq(i).children().eq(3).text('播放量：'+musicList[i]['playCount']+'  '+'收藏量：'+musicList[i]['likeCount']);
		}
    });
	


	// ===============搜索功能========================

	// 主菜单顶部菜单input回车搜索
	$("#inpSearch_main").on("keydown", function (ev) {
		var ev = ev || window.event;
		if (ev.keyCode === 13) {
			toSearchPage();
		}
	});
	// 主菜单顶部菜单query图标点击搜索
	$("#top_searchBtn_main").on("click", function () {
		toSearchPage();
	});
	// 登录功能

    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/likelist/recommend")
        .then(response => {
            let result = response.data.content;
            var divs=$(".hot-left-video>div");
            for(var i=0;i<8;i++){
                divs.eq(i).children().eq(0).attr('src',result[i]["imagePath"]);
                divs.eq(i).children().eq(1).text(result[i]['listname']);
                divs.eq(i).children().eq(3).text(result[i]['clickCount']+'w');
            }
        })
        .catch(function (error) {
            console.log(error);
            messageBox('未知异常，请联系开发人员 @_@ ','error');
        });

});

$("#refresh").on("click",function(){
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/likelist/recommend")
        .then(response => {
            let result = response.data.content;
            var divs=$(".hot-left-video>div");
            for(var i=0;i<8;i++){
                divs.eq(i).children().eq(0).attr('src',result[i]["imagePath"]);
                divs.eq(i).children().eq(1).text(result[i]['listname']);
                divs.eq(i).children().eq(3).text(result[i]['clickCount']+'w');
            }
        })
        .catch(function (error) {
            console.log(error);
            messageBox('未知异常，请联系开发人员 @_@ ','error');
        });
});


$(".rank").on("dblclick","div",function(){
	var songname=$(this).children().eq(1).text();
	var singer=$(this).children().eq(2).text();
	localStorage.setItem("songname",songname);
	localStorage.setItem("singer",singer);
	localStorage.setItem("albumName","");
	localStorage.setItem("albumAuthor","");
	location.href="./player.html";
});