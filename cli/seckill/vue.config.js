let proxyObj = {};
proxyObj['/'] = {
	ws:false,
	target:'http://localhost:30002',
	changeOrigin:true,
	pathRewrite:{
		'^/':''
	}
}
module.exports = {
	devServer:{
		host:'localhost',
		port:8080,
        proxy:proxyObj
	},
	publicPath:'./'
}