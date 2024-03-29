//========================================================
//                       歌手详情
//========================================================
function searchSingerDetail(key){
    console.log(key);
    // 请求个人简介
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/singer/searchOneSinger?singername="+key)
    .then(response => {
        let result = response.data.content;
        console.log(result);
        messageBox('获取歌手详情成功','notice');
        $("#singer_img>img").attr('src',result["imagePath"]);
        $("#singer_des>h1").text(result["singername"]);
        $("#content").text(result["description"]);
    })
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
    // 请求个人单曲
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/searchBySinger?singername="+key)
        .then(response => {
            if (response == "")
                messageBox('未知异常，请联系开发人员 @_@ ','error');
            else {
                let result = response.data.content;
                var listHtml = '';
                for (var i = 0; i < response.data.status; i++) {
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
                $("#infolist_detail").html(listHtml);
            }
        })
        .catch(function (error) {
            console.log(error);
            messageBox('未知异常，请联系开发人员 @_@ ','error');
        });
}


$("#infolist_detail").on("dblclick", "tr", function () {
    var ele=$(this).find("td");
    var key=ele.eq(2).text()+'_'+ele.eq(3).text();
    //SearchMusic(key);

    localStorage.setItem("songname",ele.eq(2).text());
    localStorage.setItem("singer",ele.eq(3).text())
    localStorage.setItem("albumName","");
    localStorage.setItem("albumAuthor","");
    location.href="./player.html";
    console.log(key);
});

// 单击收藏
$("#infolist_detail").on("click", ".fa-heart-o", function () {
    var songname=$(this).parent().next();
    var singer=songname.next();

    localStorage.setItem("songname",songname.text());
    localStorage.setItem("singername",singer.text());

    console.log(songname.text()+'_'+singer.text());
    var form_1=document.getElementsByClassName("form_1");

    searchList();

    if(localStorage.getItem("userState") == "login"){
        form_1[0].className="form_1 open";
    }
});

// 单击下载
$("#infolist_detail").on("click", ".fa-download", function () {
    var songname=$(this).parent().next();
    var singer=songname.next();
    $("#detail_dld").attr('href',"http://"+IP_ADDR+":8080/MusicPlayer/music/downloadSong?songname="+songname.text().trim()+"&singer="+singer.text().trim());
    $("#detail_dld")[0].click();
})

function searchList(){
    axios.get("http://"+IP_ADDR+":8080/MusicPlayer/user/allLikelist")
    .then(response => {
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

//========================================================
//                       收藏框
//========================================================

$(".close").click(function(){
	var form_1=document.getElementsByClassName("form_1");
	form_1[0].className="form_1";
})

$(".group").on("click", "label", function (event) {
	console.log($(this));
	var index = $(this).index();
	if ($(event.target).is('input')) {//对点击目标元素做判断,防止事件再次冒泡
		return;
	}
	if ($(".group label").eq(index).hasClass("active")) {
		$(".group label").eq(index).removeClass("active")
	} else {
		$(".group label").eq(index).addClass("active")
	}
})

$(".group").on("click", "label", function () {
	var index = $(this).index();
	var chk_value = [];//定义一个数组    
	$('input[name="parent_checkbox"]:checked').each(function () {//遍历每一个名字为interest的复选框，其中选中的执行函数    
		chk_value.push($(this).val());//将选中的值添加到数组chk_value中    
	});
})

//点击收藏
$(".submit_1").click(function(){
	var ele=$(".group label");
	var data={};

	data['songname']=localStorage.getItem("songname");
   	data['singername']=localStorage.getItem("singername");

	var idx=0;
	for(var i=0;i<ele.length;i++){
		if(ele.eq(i).hasClass("active")){
			data['key'+idx]=ele.eq(i).text().trim();
			idx++;
			ele.eq(i).removeClass("active");
		}
	}
    console.log(data);
    axios.post('http://'+IP_ADDR+':8080/MusicPlayer/likelist/likeSong', data).then(response => {
        if(response.data.status == -1){
            messageBox('收藏失败！您还未登陆','warning');
        }
        else{
            messageBox('收藏成功！','notice');
        }
    })
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
    document.getElementsByClassName("form_1")[0].className="form_1";
})

function echo_img(){
	var file=$('#upload_listimg')[0].files[0]
	var imgSrc = URL.createObjectURL(file);
	$("#img_preview").attr('src',imgSrc);
	$("#upload_list>span").text(file['name']);
}