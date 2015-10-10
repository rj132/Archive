/**
 * login
 */
$(document).ready(function(){
	//当元素失去焦点时，发生 blur 事件
	$(":input").blur(function() { //注册blur的事件  
		blueStyle("input");
    });  
	
	
	$('#btn_submit').click(function(){		
		//当用户名或密码为空时，边框设置为红色，示意用户输入完整信息
		blueStyle("input");
		var user = {
				userName: encodeURIComponent($('#uname').val()),   //对于有中文字段时编码，防止乱码
				userPwd: $('#upwd').val()
		};		
		
		if(!checkFormat(user)){			
			return;
		}
		var strUser = JSON.stringify(user); //将json对象转为字符串，用于传参  
		//当dataType为json时，返回的json对象可直接使用data['username']
		$.ajax({
			data:{action:'checkUser',params:strUser},	  //传参			  
			dataType: "json",
			url:"LoginServlet",
			success:function(data,textStatus){
				console.log("消息:" + data + " " + textStatus);
				if(data.success){
					if(data.message=='exist'){
						$('#loginModal').modal('hide');
						$('#loginModal').on('hidden.bs.modal',function(){
							$('#rspan').html("login success");//data["username"] + data["userpwd"]);						
						});
					}else{
						$('#rspan').html("login fail");
					}					
				}else{
					var msg = data.message.split(":");
					console.log("消息：" + msg[1]);
				}
				
				
			},
			error:function(XMLHttpRequest, textStatus){
				console.log("请求servlet出错");
			}				
		});
		
	});	
});
//检查输入框合法性
function checkFormat(user){
	var flag = true;
	if(user.userName==""||user.userName==undefined){		
		flag = false;
	}
	if(user.userPwd==""||user.userPwd==undefined){		
		flag = false;
	}
	return flag;
}

function blueStyle(tag){
	 $(tag).each(function() { //遍历input元素对象   
         if ("" == $(this).val().trim()) { //判断元素对象的value值                  	
             $(this).addClass("blur"); //添加css样式  
         }else{  
             $(this).removeClass("blur");  
         }  
     });  
	
}


