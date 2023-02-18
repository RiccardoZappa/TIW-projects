/**
 * 
 */
 {
	document.getElementById('userName').innerHTML = sessionStorage.getItem('userName');
	
	var meetingList, invitationList,userList;
	var pageOrchestrator = new PageOrchestrator();
	
	 window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    },false);
	
	function PageOrchestrator(){
        this.start = function(){
			meetingList = new MeetingList(
				document.getElementById("meetings"), 
                document.getElementById("meetings-message")
			);
			invitationList = new InvitationList(
				document.getElementById("invitations"),
				document.getElementById("invitations-message") 
			);
			userList = new UserList(
				document.getElementById("checkbox-user"),
				document.getElementById("checkbox-message")
			);
			}
		 this.refresh = function(){
            //Refresh view
            meetingList.show();
            invitationList.show();
            userList.show();
        };
		}
	  
	function MeetingList( _meetings, _meetings_message) {
	    
	    this.meetings = _meetings;
        this.meetings_message = _meetings_message;
		let self = this;
	     this.show = function(){
            //Request and update with the results
            makeCall("GET", 'GetMeetings', null, function (request) {
				
				if(request.readyState == XMLHttpRequest.DONE){
                
                switch(request.status){
                    case 200: //ok
                        var meetings = JSON.parse(request.responseText);
                        self.update(meetings);
                        break;
                    case 400: // bad request
                    case 401: // unauthorized
                    case 500: // server error
                        self.update(null, request.responseText);
                        break;
                    default: //Error
                        self.update(null, "Request reported status " + request.status);
                        break;
                }
                }
               } 
            );
            
        };
        this.update = function(_meetings, _error) {
            
            self.meetings.style.display = "none";
            self.meetings.innerHTML = "";
            self.meetings_message.textContent = "Your meetings:";
			
            if(_error){

                self.meetings_message.textContent = _error;
                if(!self.meetings_message.className.includes("warning-message"))
                    self.meetings_message.className += " warning-message";
                self.meetings_message.style.display = "block";
                
            }else{

                if(_meetings.length === 0){
                    if(self.meetings_message.className.includes("warning-message"))
                        self.meetings_message.className.replace(" warning-message", "");
                    	self.meetings_message.textContent = "You have created 0 meetings :(";
                   		self.meetings_message.style.display = "block";
                    }else{
                    	
                    	let card, card_title, card_data, b1, b2, br;
                    	 _meetings.forEach((meet) => {
							card = document.createElement("div");
                        	card.className = "card card-blue";
                        	card_title = document.createElement("div");
                        	card_title.className = "card-title";
                        	card_title.textContent = meet.title;
                        	card.appendChild(card_title);
                        	card_data = document.createElement("div");
                       		card_data.className = "card-data";

                        	b1 = document.createElement("b");
                        	b1.textContent = "Start date: ";
                        	card_data.appendChild(b1);
                        	card_data.appendChild(document.createTextNode(meet.start_date));
                    
                        	br = document.createElement("br");
                        	card_data.appendChild(br);

                        	b2 = document.createElement("b");
                        	b2.textContent = "End date: ";
                        	card_data.appendChild(b2);
                        	card_data.appendChild(document.createTextNode(meet.end_date ));
                        	card.appendChild(card_data);
                        	self.meetings.appendChild(card);
								
						});
        				self.meetings.style.display = "block";
        				};
        		};
        	};
        };
        function InvitationList( _invitations, _invitations_message) {
	    
	    this.invitations = _invitations;
        this.invitations_message = _invitations_message;
		var self = this;

	     this.show = function(){
            //Request and update with the results
            makeCall("GET", 'GetInvitations', null, function (request){
				if(request.readyState == XMLHttpRequest.DONE){
                switch(request.status){
                    case 200: //ok
                        var invitations = JSON.parse(request.responseText);
                        self.update(invitations);
                        break;
                    case 400: // bad request
                    case 401: // unauthorized
                    case 500: // server error
                        self.update(null, request.responseText);
                        break;
                    default: //Error
                        self.update(null, "Request reported status " + request.status);
                        break;
                }
                }
            });
        };
        this.update = function(_invitations, _error) {
            
            self.invitations.style.display = "none";
            self.invitations.innerHTML = "";
            self.invitations_message.textContent = "Your invitations:";

            if(_error){

                self.invitations_message.textContent = _error;
                if(!self.invitations_message.className.includes("warning-message"))
                    self.invitations_message.className += " warning-message";
                self.invitations_message.style.display = "block";
                
            }else{

                if(_invitations.length === 0){
                    if(self.invitations_message.className.includes("warning-message"))
                        self.invitations_message.className.replace(" warning-message", "");
                    self.invitations_message.textContent = "You have 0 meeting invitations:(";
                    self.invitations_message.style.display = "block";
                    }else{
                    	
                    	let card, card_title, card_data, b1, b2,b3, br,br2;
                    	 _invitations.forEach(function(invit){
							card = document.createElement("div");
                        	card.className = "card card-blue-invitation";
                        	card_title = document.createElement("div");
                        	card_title.className = "card-title-invitation";
                        	card_title.textContent = invit.title;
                        	card.appendChild(card_title);
                        	card_data = document.createElement("div");
                       		card_data.className = "card-data-invitation";

                        	b1 = document.createElement("b");
                        	b1.textContent = "Start date: ";
                        	card_data.appendChild(b1);
                        	card_data.appendChild(document.createTextNode(invit.start_date));
                    
                        	br = document.createElement("br");
                        	card_data.appendChild(br);

                        	b2 = document.createElement("b");
                        	b2.textContent = "End date: ";
                        	card_data.appendChild(b2);
                        	card_data.appendChild(document.createTextNode(invit.end_date));
                        	
                        	br2 = document.createElement("br");
                        	card_data.appendChild(br2);
								
							b3 = document.createElement("b");
                        	b3.textContent = "us-creator: ";
                        	card_data.appendChild(b3);
                        	card_data.appendChild(document.createTextNode(invit.us_creator));
                        	card.appendChild(card_data);
                        	self.invitations.appendChild(card);
						});
        				self.invitations.style.display = "block";
        				};
        		};
        	};
        };
		
		function UserList(_users,_checkbox_message){
		this.users = _users;
		this.checkbox_message = _checkbox_message;
		var self = this;
	    this.reset = function() {
	      this.listcontainer.style.visibility = "hidden";
	    }

	     this.show = function(){
            //Request and update with the results
            makeCall("GET", 'GetUsers', null, function (request){
				if(request.readyState == XMLHttpRequest.DONE){
                switch(request.status){
                    case 200: //ok
                        var users = JSON.parse(request.responseText);
                        self.update(users);
                        break;
                    case 400: // bad request
                    case 401: // unauthorized
                    case 500: // server error
                        self.update(null, request.responseText);
                        break;
                    default: //Error
                        self.update(null, "Request reported status " + request.status);
                        break;
                }
                }
            });
         this.update = function(_users,_error){
			self.users.style.display = "none";
            self.users.innerHTML = "";

            if(_error){

                self.checkbox_message.textContent = _error;
                if(!self.checkbox_message.className.includes("warning-message"))
                    self.checkbox_message.className += " warning-message";
                self.checkbox_message.style.display = "block";
                
            }else{

                if(_users.length === 0){
                    if(self.checkbox_message.className.includes("warning-message"))
                        self.checkbox_message.className.replace(" warning-message", "");
                    self.checkbox_message.textContent = "there are no users :(";
                    self.checkbox_message.style.display = "block";
                    }else{
                    	self.checkbox_message.style.display = "none";
                    	let checkbox,br;
                    	let container = document.getElementById('checkbox-user');
						_users.forEach((user)=> {
							label = document.createElement('label');
  							text = document.createTextNode(user.username + ": ");
  							label.appendChild(text);
							checkbox = document.createElement("input");
    						checkbox.type = "checkbox";
    						checkbox.value = user.username;
    						checkbox.name = "username[]";
    						checkbox.addEventListener('change', function() {
        					console.log(`${this.value} checkbox was ${this.checked ? 'checked' : 'unchecked'}.`);
    						});
    						br = document.createElement('br');
    						container.appendChild(label);
    						container.appendChild(checkbox);
    						container.appendChild(br);
	
						});
       					self.users.style.display = "block";
       				}	
			
				}
       		}
       		}
       		}
       		const modal = document.querySelector(".modal");
			const overlay = document.querySelector(".overlay");
			const openModalBtn = document.querySelector(".btn-open");
			const closeModalBtn = document.querySelector(".btn-close");
       
       		const openModal = function () {
  				modal.classList.remove("hidden");
  				overlay.classList.remove("hidden");
			};
			openModalBtn.addEventListener("click", openModal);
			const closeModal = function () {
  				modal.classList.add("hidden");
  				overlay.classList.add("hidden");
			};
			closeModalBtn.addEventListener("click", closeModal);
			overlay.addEventListener("click", closeModal);
			let counter = 0;
			
			document.getElementById("meetingbtn").addEventListener("click", (e) =>{
				e.preventDefault();
				console.log("Create Meeting!");
		
				var form = e.target.closest("form");
				var formcontent = form.querySelector("#title").value;
				var userCheckboxes = form.querySelectorAll("input[name='username[]']:checked");
				console.log(userCheckboxes.length);
				console.log(formcontent);
				console.log(userCheckboxes);
				if (userCheckboxes.length > 10){
					document.getElementById("error").textContent = "meetings must have max 10 partecipants!";
					counter ++;
				}
  				if (counter == 3){
					document.getElementById("errorModal").textContent = "3 attempts creating a meeting with more than 10 partecipants!";
					counter = 0;
					form.reset();
					document.getElementById("error").textContent = "";
					closeModal(); 
					
				}
				if (userCheckboxes.length == 0){
					document.getElementById("error").textContent = "no partecipants selected!";
					
				}
  				if(userCheckboxes.length != 0 && userCheckboxes.length <= 10 ){

           			 //Make the call to the server
           			 makeCall("POST" , 'CreateMeetings' , form ,
           	    	 function (x) {

                    	if(x.readyState == XMLHttpRequest.DONE){
							var message = x.responseText;
							switch(x.status){
								case 200:
								document.getElementById("errorModal").textContent ="";
								closeModal();
                                pageOrchestrator.refresh();
                          
                                break;
                                case 400:
                                document.getElementById("error").textContent = message;
                                break;
                                default:
                                document.getElementById("error").textContent = "error with the call to the server";
                                break;
                        	}
                   	 	}	
                	});
            	}else{
            		form.reportValidity();
        		}
            	});
            	
			
};