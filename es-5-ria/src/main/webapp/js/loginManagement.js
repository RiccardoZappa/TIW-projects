/**
 * Login
 */
(function() {
    document.getElementById("loginButton").addEventListener('click' , (e) => {

        console.log("Login event!");
        //Take the closest form
        let form = e.target.closest("form");
		
        //Check if the form is valid -> every field is been filled
        if(form.checkValidity()){
			let username = document.getElementById("username").value;
			let password = document.getElementById("password").value;
			if(username.length == 0 || password.length == 0 ){
                document.getElementById("error").textContent = "username or password null";
                return;
            }

            //Make the call to the server
            makeCall("POST" , 'Login' , form ,
                function (x) {

                    if(x.readyState == XMLHttpRequest.DONE){
                        let message = x.responseText;
                        switch(x.status){
                            //If ok -> set the userName in the session
                            case 200:
                                sessionStorage.setItem('userName' , message);
                                window.location.href = "home.html";
                                break;
                            //If ko -> show the error
                            default:
                                document.getElementById("error").textContent = message;
                                break;
                        }
                    }
                }
            );
        }else{
            form.reportValidity();
        }
    });
})();