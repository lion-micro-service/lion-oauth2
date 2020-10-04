<template>
    <a-modal destroyOnClose v-model="addOrUpdateModal" width="800px" title="添加/修改客户端" centered @ok="addOrUpdate" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <a-form-model layout="inline" ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
            <a-row>
                <a-col :span="12">
                    <a-form-model-item label="客户端id" prop="clientId" ref="clientId">
                        <a-input placeholder="请输入客户端id" v-model="addOrUpdateModel.clientId" />
                    </a-form-model-item>
                </a-col>
                <a-col :span="12">
                    <a-form-model-item label="客户端密钥" prop="clientSecretPlaintext" ref="clientSecretPlaintext">
                        <a-input placeholder="请输入客户端密钥" v-model="addOrUpdateModel.clientSecretPlaintext" />
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-model-item label="token有效期" prop="accessTokenValidity" ref="accessTokenValidity">
                        <a-input-number placeholder="请输入token有效期" v-model="addOrUpdateModel.accessTokenValidity"/>
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-model-item label="授权方式" prop="grantTypes" ref="grantTypes">
                        <a-select  :default-value="addOrUpdateModel.grantTypes" @change="grantTypesChange" mode="multiple" style="width: 100%" placeholder="请选择授权方式" >
                            <a-select-option :key="grantTypes.name.toLowerCase()" v-for="(grantTypes) in authorizedGrantTypes">
                                {{ grantTypes.name.toLowerCase()}}
                            </a-select-option>
                        </a-select>
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-model-item label="权限" prop="scopes" ref="scopes">
                        <a-select :default-value="addOrUpdateModel.scopes" @change="scopesChange" mode="tags" placeholder="请选择权限" style="width: 100%" :token-separators="[',']">
                            <a-select-option :key="scope.name.toLowerCase()" v-for="(scope) in scopes">
                                {{ scope.name.toLowerCase()}}
                            </a-select-option>
                        </a-select>
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-model-item label="资源" prop="resourceIds" ref="resourceIds">
                        <a-textarea  placeholder="请输入资源(逗号隔开)" :rows="6" v-model="addOrUpdateModel.resourceIds"/>
                    </a-form-model-item>
                </a-col>
            </a-row>
        </a-form-model>
    </a-modal>
</template>

<script lang="ts">
    import {Component,  Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-front-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Component({})
    export default class addOrUpdate extends Vue{
        //点击阴影层是否关闭窗口
        private maskClosable:boolean=false;
        //是否显示窗口
        private addOrUpdateModal:boolean=false;
        //添加修改数据模型
        private addOrUpdateModel:any={};
        //授权方式数据源
        private authorizedGrantTypes:Array<any>=[];
        //授权方式默认选中数据
        private grantTypesDefault:Array<string>=[];
        //权限数据源
        private scopes:Array<any>=[];
        //权限默认选中数据
        private scopesDefault:Array<string>=[];
        //校验规则
        private rules:any={
            clientId:[{required:true,validator:this.checkClientIdIsExist,trigger:'blur'}],
            clientSecretPlaintext:[{required:true,message:"请输入客户端密钥",trigger:'blur'}],
            accessTokenValidity:[{required:true,message:"请输入token有效期",trigger:'blur'}],
        };



        /**
         * 检查客户端id是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkClientIdIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入编码'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/oauth/client/console/check/clientId/exist",{params:{"clientId":this.addOrUpdateModel.clientId,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data.isExist) {
                            callback(new Error('客户端id已存在'));
                        }else {
                            callback();
                        }
                    })
                    .catch(fail => {
                    })
                    .finally(()=>{
                    });
                return;
            }
            callback();
        }

        /**
         * 提交数据
         */
        private addOrUpdate():void{
            (this.$refs.addOrUpdateForm as any).validate((validate: boolean) => {
                if (validate) {
                    this.addOrUpdateModel.authorizedGrantTypes=this.addOrUpdateModel.grantTypes.join(",");
                    this.addOrUpdateModel.scope=this.addOrUpdateModel.scopes.join(",");
                    if (this.addOrUpdateModel.id){
                        axios.put("/oauth/client/console/update",this.addOrUpdateModel)
                            .then((data) =>{
                                if (Object(data).status === 200){
                                    message.success(Object(data).message);
                                    this.success();
                                }
                            }).catch((fail)=>{
                        }).finally(()=>{
                        })
                    }else {
                        axios.post("/oauth/client/console/add",this.addOrUpdateModel)
                            .then((data) =>{
                                if (Object(data).status === 200){
                                    message.success(Object(data).message);
                                    this.success();
                                }
                            }).catch((fail)=>{
                        }).finally(()=>{
                        })
                    }
                }
            });
        }



        /**
         * 获取详情
         * @param id
         */
        private async getDetails(id:string){
            await axios.get("/oauth/client/console/details",{params:{"id":id}})
            .then((data)=>{
                if (Object(data).status === 200 && data.data.oauthClientDetails){
                    this.addOrUpdateModel=data.data.oauthClientDetails;
                    this.addOrUpdateModel.scopes=data.data.oauthClientDetails.scope.split(',');
                    this.addOrUpdateModel.grantTypes=data.data.oauthClientDetails.authorizedGrantTypes.split(',');
                }
            })
            .catch(fail => {
            })
            .finally(()=>{
            });
        }

        /**
         * 获取权限下拉框数据源
         */
        private async getSelectDate(){
            await axios.get("/common/enum/console/to/select", {params: {"enumClass": "com.lion.resource.enums.Scope"}})
            .then((data) => {
                this.scopes = data.data.enum;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
            await axios.get("/common/enum/console/to/select", {params: {"enumClass": "com.lion.resource.enums.GrantTypes"}})
            .then((data) => {
                this.authorizedGrantTypes = data.data.enum;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
        }

        /**
         * 弹出窗口
         */
        private async openWindow(id:string) {
            this.addOrUpdateModel={};
            const _this=this;
            await this.getSelectDate();
            if (id) {
                await this.getDetails(id);
            }
            setTimeout(function () {
                _this.addOrUpdateModal=true;
            },500);

        }

        /**
         * 提交数据成功后事件
         */
        private success():void{
            this.addOrUpdateModal = false;
            this.addOrUpdateModel={};
            (this.$parent as any).search();
        }

        /**
         * 授权方式改变事件
         * @param value
         */
        private grantTypesChange(value:Array<string>):void{
            this.addOrUpdateModel.grantTypes = value;
        }

        /**
         * 权限改变事件
         * @param value
         */
        private scopesChange(value:Array<string>):void{
            this.addOrUpdateModel.scopes = value;
        }

    }
</script>

<style scoped>
    .ant-form-item >>> .ant-form-item-label{
        width: 100px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 100px);
    }
</style>