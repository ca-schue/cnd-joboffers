const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const webpack = require("webpack");

module.exports = merge(common, {
    mode: 'production',
    plugins: [
        new webpack.DefinePlugin({
            'process.env':{
                'APP_ENV': JSON.stringify('prod'),
                'AUTH_SERVICE_URL': JSON.stringify('http://auth-service:8080'),
                'USER_SERVICE_URL': JSON.stringify('http://user-service:8080'),
                'CAREER_SERVICE_URL': JSON.stringify('http://career-service:8080')
            }
        }),
    ],
});
