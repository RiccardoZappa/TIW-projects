/**
 * Registration
 */
(function () {
    document.getElementById("registrationButton").addEventListener('click' , (e) => {

        console.log("Registration event!");
        //Take the closest form
        let form = e.target.closest("form");
        //Reset the error
        document.getElementById("error").textContent = null;

        //Check the form validity
        if(form.checkValidity()){
			
            //Check the validity of the fields
            let username = document.getElementById("username").value;
            let email = document.getElementById("email").value;
            let password = document.getElementById("password").value;
			let confirmpassword = document.getElementById("passwordconfirm").value;
			
            if(username.length == 0 || password.length == 0 || email.lenght == 0){
                document.getElementById("error").textContent = "username, email or password null";
                return;
            }
            if(password != confirmpassword){
                document.getElementById("error").textContent = "Passwords are not the same";
                return;
            } 
            console.log("Validity ok");

            //Make the call to  the server
            makeCall("POST" , 'Register' , form ,
                function (x) {

                    if(x.readyState == XMLHttpRequest.DONE){
						let message = x.responseText;
                        switch(x.status){
                            //If ok -> redirect to home page
                            case 200:
                            	sessionStorage.setItem('userName' , message);
                                window.location.href = "home.html";
                                break;
                            //If ko -> show the error
                            default:
                                document.getElementById("error").textContent = x.responseText;
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