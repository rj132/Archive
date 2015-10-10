/**
 *查询
 */
$(function(){
	
	$('#search').click(function(){		
		$.ajax({
			url:'SearchServlet',
			data:{action:"getAll"},
			dataType:"json",
			success:function(data,textStatus){
					if(data.success){					
						for(var i=0;i<data.result.length;i++){
							//console.log("msg:" + data.result[i].susername + " " +data.result[i].staffNo);
							var td1 = $("<td>"+data.result[i].suserName +"</td>");
							var td2 = $("<td>"+data.result[i].staffPwd+"</td>");
							var td3 = $("<td>"+data.result[i].staffNo+"</td>");
							var td4 = $("<td>"+data.result[i].realName+"</td>");
							var td5 = $("<td>"+data.result[i].staffGrant+"</td>");
							var td6 = $("<td>"+data.result[i].unitCode+"</td>");
							var tr = $("<tr></tr");
							tr.append(td1);
							tr.append(td2);
							tr.append(td3);
							tr.append(td4);
							tr.append(td5);
							tr.append(td6);
							$('#tblStaffInfo').append(tr);
						}					
					}else{
						if(data.message.indexOf(":")==-1){
							console.log("消息：" + data.message);
						}else{
							var msg = data.message.split(":");
							console.log("消息：" + msg[1]);
						} 
						
					}					
				},
			error:function(XMLHttpRequest, textStatus){
				console.log("请求searchServlet出错");
			}
		});		
		
	});
	
});