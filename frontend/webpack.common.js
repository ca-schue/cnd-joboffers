const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin')
const ESLintPlugin = require('eslint-webpack-plugin')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')
const Dotenv = require('dotenv-webpack')
const CopyPlugin = require("copy-webpack-plugin");

module.exports = {
    output: {
        path: path.resolve(__dirname, 'build'),
        filename: '[name].[contenthash].bundle.js',
        clean: true
    },
    resolve: {
        modules: [path.join(__dirname, 'src'), 'node_modules'],
        extensions: ['.tsx', '.ts', '.js'],
        alias: {
            react: path.join(__dirname, 'node_modules', 'react')
        }
    },
    devtool: 'source-map',
    entry: './src/index.tsx',
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {
                test: /\.(ts|tsx)?$/,
                exclude: /node_modules/,
                use: 'ts-loader'
            },
            {
                test: /\.css$/,
                use: [
                    {
                        loader: 'style-loader'
                    },
                    {
                        loader: 'css-loader'
                    }
                ]
            },
            {
                test: /\.(jpg|png|svg|mp3|gif)$/,
                loader: 'file-loader'
            },
        ]
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: './src/index.html',
            favicon: "./src/assets/icon.ico"
        }),
        new CopyPlugin({
            patterns: [
                { from: "src/assets", to: "assets" }
            ],
        }),
        new ESLintPlugin(),
        new NodePolyfillPlugin(),
        new Dotenv()
    ],
    devServer: {
        historyApiFallback: true
    }
}
