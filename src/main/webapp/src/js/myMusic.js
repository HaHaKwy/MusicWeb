/**
  * Theme: index Utils
  * Dependence: jQuery
  *
  */
var styleActive=function (eles,eventType,className) {
// 利用冒泡原理给新DOM也添加事件
	if (eles instanceof Array) {
		eles[0].on(eventType,eles[1],function () {
			eles[0].find(eles[1]).each(function (index,item) {
				$(item).removeClass(className);
			});
			$(this).addClass(className);
		});
	} else {
		eles.on(eventType,function () {
			eles.each(function (index,item) {
				$(item).removeClass(className);
			});
			$(this).addClass(className);
		});
	}
}
function toSearchPage(){
	var strSearch = $("#inpSearch_main").val();
	$.trim(strSearch);
	localStorage.setItem('flag', '1');
	localStorage.setItem('key', strSearch);
	location.href='./searchPage.html';
}
$(function () {

	// =========================初始化=====================================

	// 清空所有本地存储数据
	sessionStorage.clear();
	localStorage.setItem('flag','0');
	sessionStorage.setItem("commentNum","0")

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
	// ===========================基础交互样式====================================
	
	// tab选项卡切换样式
	styleActive([$(".R_page .tabbtns"),".label_btn"],"click","active");
	// list切换样式
	styleActive([$("#listContainer>.list>.btngroups"),".btn"],"click","active");
	// tr切换样式
	styleActive([$(".infolist"),"tr"],"click","active");

	

	// 收起 | 展开 歌单列表
	$("#listContainer>.list>.title .unfoldlist").on("click",function () {
		var $btnGroups=$(this).parents(".list").find(".btngroups");
		if ($btnGroups.css("display")==="block") {
			$btnGroups.slideUp(500);
			$(this).html('<i class="fa fa-angle-right" aria-hidden="true"></i>');
		} else {
			$btnGroups.slideDown(500);
			$(this).html('<i class="fa fa-angle-down" aria-hidden="true"></i>');
		}
	});

});


$("#comment_tab").on("click", function(){
	var album=localStorage.getItem('albumName');
	var author=localStorage.getItem('albumAuthor');
	let commentNum = 0;
	axios.get("http://"+IP_ADDR+":8080/MusicPlayer/comment/allComment?listname="+album+"&creator="+author)
	.then(response => {
		var result = response.data.content;
		var newComment="";
		for (var i = 0; i < response.data.status; i++){
			console.log("result:i="+i)
			newComment +=
				`<div class="content">
					<p><span class="username"><a href="javascript:void(0);">`+result[i]['username']+`</a></span>：<span class="usersay">`+result[i]['text']+`</span></p>
					<div class="btngroups clearfix">
						<span class="time">`+result[i]['timestamp']+`</span>
						<span class="btn">
						<a href="javascript:void(0);" class="btn_report">举报</a>&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="javascript:void(0);" class="btn_support"><i class="fa fa-thumbs-o-up" aria-hidden="true"></i>(0)</a>&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="javascript:void(0);" class="btn_share">分享</a>&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="javascript:void(0);" class="btn_reply">回复</a>
						</span>
					</div>
				</div>`;
		}
		console.log(newComment)
		sessionStorage.setItem("commentNum",String(response.data.status))
		newComment = `<div class="title">最新评论(0)</div>`+newComment;
		$("div.comment.new").html(newComment);
		$(".comment.new>div.title").text("最新评论("+String(response.data.status)+")");
	})
	.catch(function (error) {
	    console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
	});
});

$("a.btn.comment").on("click",function(){
	var album=localStorage.getItem('albumName');
	var author=localStorage.getItem('albumAuthor');
	let userName=localStorage.getItem('userName');
	let time = new Date();
	var commentText=$("#commentInput").val();
	console.log(commentText);
	var newComment=`
		<div class="content">
			<p><span class="username"><a href="javascript:void(0);">`+userName+`</a></span>：<span class="usersay">`+commentText+`</span></p>
			<div class="btngroups clearfix">
				<span class="time">`+time.toLocaleString()+`</span>
				<span class="btn">
				<a href="javascript:void(0);" class="btn_report">举报</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				<a href="javascript:void(0);" class="btn_support"><i class="fa fa-thumbs-o-up" aria-hidden="true"></i>(0)</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				<a href="javascript:void(0);" class="btn_share">分享</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				<a href="javascript:void(0);" class="btn_reply">回复</a>
				</span>
			</div>
		</div>`;
	//post
	axios.post('http://'+IP_ADDR+':8080/MusicPlayer/comment/publishComment', {
	"listname": album,
	"creator": author,
	"text": commentText,
	"timestamp": time.toLocaleString()
	}).then(response => {
	    messageBox('发表评论成功！','notice');
		$("div.comment.new").append(newComment);
		let Num = Number(sessionStorage.getItem("commentNum"))+1;
		$(".comment.new>div.title").text("最新评论("+String(Num)+")");
		$("#commentInput").val('');
	})
	.catch(function (error) {
        console.log(error);
          messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
});
// 请求歌单信息
function getTitle(key){
	axios.get("http://"+IP_ADDR+":8080/MusicPlayer/likelist/likelistInfo?listname="+key)
    .then(response => {
        let result = response.data.content;
        $("#playlist_listName").text(result['listname']);
		$("#playlist_userName").text(result['username']);
		$("#playlist_createTime").text(result['creationDate']);
		$("#playlist_listPic").attr("src",result['imagePath']);
		$("#detail_info_content").text(result['description']);
    })
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}

// 请求个人歌单
function getSelfList(){
	axios.get("http://"+IP_ADDR+":8080/MusicPlayer/user/allLikelist")
    .then(response => {
        let result = response.data.content;
        var listHtml ='';
        for (var i = 0; i < response.data.status; i++) {
            listHtml += `<p class="btn list_create_001"><i class="fa fa-music" aria-hidden="true"></i>&nbsp;&nbsp;`
        					+result[i]+`</p>
        				`;
        }
        $(".btngroups").html(listHtml);
        $(".list>.btngroups>.btn").eq(0).addClass("active");
        getTitle($(".list>.btngroups>.btn.active").text().trim());
        getListSongs($(".list>.btngroups>.btn.active").text().trim());
    })
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}

// 选择歌单
$(".btngroups").on("click", ".list_create_001", function () {
	$(".list>.btngroups>.btn.active").removeClass("active");
	// $(this).children().eq(0).addClass("active");
	$(this).addClass("active");
	getTitle($(".list>.btngroups>.btn.active").text().trim());
	getListSongs($(".list>.btngroups>.btn.active").text().trim());
	localStorage.setItem('albumName', String($(".list>.btngroups>.btn.active").text().trim()));
	$("#song_list").click();
})


// 请求歌单歌曲
function getListSongs(key){
	axios.get("http://"+IP_ADDR+":8080/MusicPlayer/likelist/likelistContent?listname="+key)
    .then(response => {
        let result = response.data.content;
		var listHtml = '';
		for (var i = 0; i < response.data.status; i++) {
            var minute = '0'+Math.trunc(result[i]['length']/60);
            var second = (result[i]['length']%60 < 10) ? '0'+result[i]['length']%60:result[i]['length']%60;
			listHtml += `
					<tr>
						<td class="index">`+((+i + 1) < 10 ? "0" + (+i + 1) : (+i + 1)) +`</td>
						<td><i class="fa fa-heart-o" aria-hidden="true"></i>&nbsp;
						<i class="fa fa-download" aria-hidden="true"></i>
						</td>
						<td>`+result[i]['songname']+`</td>
						<td>`+result[i]['singer']+`</td>
						<td>`+result[i]['album']+`</td>
                        <td>`+ minute+':'+second + `</td>
					</tr>`
		}
		$("#infoList_playlist").html(listHtml);
    })
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}

// 创建歌单
$(".title_btngroups").on("click", ".fa-plus-circle", function () {
	// createList("tmp");
	console.log("click");
	var form_1=document.getElementsByClassName("form_1");
	form_1[0].className="form_1 open";
})

$("#btn_create").on("click", function () {
	var data=new FormData();
	data.append('listname',document.getElementById("input_listname").value);
	data.append('description',document.getElementById("input_description").value);
	var file=$('#upload_listimg')[0].files[0]
	data.append('file',file);

	createList(data,document.getElementById("input_listname").value);

	var form_1=document.getElementsByClassName("form_1");
    form_1[0].className="form_1";
});

function createList(data,key) {
	axios.post('http://'+IP_ADDR+':8080/MusicPlayer/likelist/creatLikelist',
	    data
	).then(response => {
	    messageBox('创建歌单成功！','notice');
		var listHtml = `
			<p class="btn list_create_001"><i class="fa fa-music" aria-hidden="true"></i>&nbsp;&nbsp;`
			+ key + `</p>
		`;
		$(".btngroups").append(listHtml);
	})
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}

$("#infoList_playlist").on("dblclick","tr",function(){
    var ele=$(this).find("td");
    var key=ele.eq(2).text()+'_'+ele.eq(3).text();
	var listname=$(".list_create .active>p.active").text().trim();
    localStorage.setItem("songname",ele.eq(2).text());
    localStorage.setItem("singer",ele.eq(3).text())
    localStorage.setItem("albumName",listname);
    location.href="./player.html";
})

// 单击下载
$("#infoList_playlist").on("click", ".fa-download", function () {
    var songname=$(this).parent().next();
    var singer=songname.next();
    $("#mymusic_dld").attr('href',"http://"+IP_ADDR+":8080/MusicPlayer/music/downloadSong?songname="+songname.text().trim()+"&singer="+singer.text().trim());
	$("#mymusic_dld")[0].click();

})