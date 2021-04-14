const pages = require('@lion/lion-frontend-core/src/webpack/vue.config');
const webpack=require('webpack');
const CompressionPlugin = require("compression-webpack-plugin");
const TerserPlugin = require('terser-webpack-plugin')
module.exports = {
    devServer: {
        disableHostCheck: true
    },
    filenameHashing: true,
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $:"jquery",
                jQuery:"jquery",
                "windows.jQuery":"jquery"
            })
        ],
        output: {
            filename: `js/[name].[hash].js`,
            chunkFilename: `js/[name].[hash].js`
        },
    },
    "pages":pages.pages(),
    "css": {
        loaderOptions: {
            less: {
                lessOptions: {
                    javascriptEnabled: true
                }
            }
        }
    },
    chainWebpack: (config) => {
        config.plugin('compressionPlugin').use(new CompressionPlugin({
            test: /\.(js|css|less)$/, // 匹配文件名
            threshold: 10240, // 对超过10k的数据压缩
            minRatio: 0.8,
            deleteOriginalAssets: false // 删除源文件
        }));
        config.plugin("terserPlugin").use((new TerserPlugin()));
    }

}

