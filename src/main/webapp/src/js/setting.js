let starttime = 0;
let valTime = 0;
let minutes = 3;
let seconds = 0;
let dificultCreateTime = [700,1000];
let dificultSpeed = 40;
let tapRule = null;
let songname = null;
let singer = null;
let line = 0;
let media = $("#Au").get(0);
$(function() {
    $('.setting-wrap').on('click', function() {
        $('.setting-nav').css('visibility', 'visible');
        $('.start').hide();
    });
    $('.dificult').on('click', function() {
        $(this).addClass('select');
        $(this).siblings('.dificult').removeClass('select');
        songname = $(this).children().eq(0).text();
        singer = $(this).children().eq(2).text();
        axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/play?songname="+songname+"&singer="+singer)
            .then(response => {
                let result = response.data.content;
                $("#Au").attr("src", result['path']);
                valTime = parseInt(result['length'])+5;
                minutes = valTime / 60;
                seconds = valTime % 60;
            })
            .catch(function (error) {
                console.log(error);
                alert("搜索失败。");
            });
        axios.get("http://"+IP_ADDR+":8080/MusicPlayer/music/beats?songname="+songname+"&singer="+singer)
        .then(response => {
            let result = response.data.content;
            tapRule = new Array();
                for (var i = 0; i < response.data.status; i++) {
                    tapRule[i] = new Array();
                    tapRule[i][0] = parseFloat(result[i]['time']);
                    tapRule[i][1] = parseInt(result[i]['lrc']);
                    console.log(tapRule[i]);
                }
            }
        )
        .catch(function (error) {
            console.log(error);
            alert("搜索失败。");
        });
        new Setting().change();
    });

});
window.onload = function(){$(".dificult")[0].click();}