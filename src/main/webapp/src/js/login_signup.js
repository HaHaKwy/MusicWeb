const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

function to_login() {
	var ele_name = document.forms["signin"]["username"];
	var ele_psw = document.forms["signin"]["password"];

	axios.post('http://'+IP_ADDR+':8080/MusicPlayer/user/Login_servlet', {
		"username": ele_name.value,
		"password": ele_psw.value
	}).then(response => {
	    if(response.data.status == -1){
	        messageBox(response.data.message,'error');
	    }
	    else{
	        window.location.href='./index.html'
	    }
//        messageBox('登陆成功！','notice');
	})
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}

function to_signup() {
	var ele_name = document.forms["signup"]["username"];
	var ele_psw = document.forms["signup"]["password"];
	var ele_eml = document.forms["signup"]["email"];

	axios.post('http://'+IP_ADDR+':8080/MusicPlayer/user/Signup_servlet', {
		"username": ele_name.value,
		"password": ele_psw.value,
		"email": ele_eml.value
	}).then(response => {
        if(response.data.status == -1){
            messageBox(response.data.message,'error');
        }
        else{
            messageBox(response.data.message,'notice');
            signInButton.click();
        }
	})
    .catch(function (error) {
        console.log(error);
        messageBox('未知异常，请联系开发人员 @_@ ','error');
    });
}