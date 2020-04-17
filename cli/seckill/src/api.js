import axios from 'axios';
import { Message } from 'element-ui';
import router from './router/index.js'
axios.interceptors.response.use(success=>{
	if(success.status&&success.status==200&&success.data.status==500)
	{
		Message.error({message:success.data.msg});
		return;
	}
	if(success.data.msg)
	{
		Message.success({message:success.data.msg});
	}
	return success.data;
},
error=>{
	
	if(error.response.status==504||error.response.status==404)
	{
		Message.error({message:'服务器内部错误或请求资源不存在'})
	}
	else if(error.response.status==403)
	{
		Message.error({message:'权限不足'});
	}
	else if(error.response.status==401){
		Message.error({message:'未登录'});
		router.replace('/');
	}
	else{
		if(error.response.data.msg)
		{
			Message.error({message:error.response.data.msg});
		}
		else{
			Message.error('未知错误');
		}
	}
	return;
});


var base = "";
export const postKeyValueRequest = (url,param)=>
{
	return axios(
				{
				method:'post',
				url:`${base}${url}`,
				data:param,
				transformRequest:[
					
					function(data)
					{
						
						let i = '';
						for(let k in data)
						{
							i += encodeURIComponent(k)+'='+encodeURIComponent(data[k])+'&';
						}
						console.log(i);
						
						return i;
					}
				],
				headers:{'Content-Type':'application/x-www-form-urlencoded'},
				});
}

export const postRequest = (url,param)=>{
	return axios({
		method:'post',
		url:`${base}${url}`,
		data:param
	});
}
	
export const putRequest = (url,param)=>{
		return axios({
			method:'put',
			url:`${base}${url}`,
			data:param
		});
}		
export const getRequest = (url,param)=>{
			return axios({
				method:'get',
				url:`${base}${url}`,
				data:param
			});
}
export const deleteRequest = (url,param)=>{
				return axios({
					method:'delete',
					url:`${base}${url}`,
					data:param
				});
}	
