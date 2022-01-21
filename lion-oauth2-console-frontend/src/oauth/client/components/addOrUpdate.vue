<template>
    <a-modal v-model:visible="addOrUpdateModal" width="800px" title="添加/修改客户端" @cancel="cancel" centered @ok="addOrUpdate" :maskClosable="maskClosable" cancelText="关闭" okText="保存">
        <a-form ref="addOrUpdateForm" :model="addOrUpdateModel" :rules="rules" >
            <a-row>
                <a-col :span="12">
                    <a-form-item label="客户端id" name="clientId" ref="clientId">
                        <a-input placeholder="请输入客户端id" v-model:value="addOrUpdateModel.clientId" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户端密钥" name="clientSecretPlaintext" ref="clientSecretPlaintext">
                        <a-input placeholder="请输入客户端密钥" v-model:value="addOrUpdateModel.clientSecretPlaintext" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-item label="token有效期" name="accessTokenValidity" ref="accessTokenValidity">
                        <a-input-number placeholder="请输入token有效期" v-model:value="addOrUpdateModel.accessTokenValidity"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-item label="授权方式" name="grantTypes" ref="grantTypes">
                        <a-select  :default-value="addOrUpdateModel.grantTypes" @change="grantTypesChange" mode="multiple" style="width: 100%" placeholder="请选择授权方式" >
                            <a-select-option :key="grantTypes.name.toLowerCase()" v-for="(grantTypes) in authorizedGrantTypes">
                                {{ grantTypes.name.toLowerCase()}}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-item label="权限" name="scopes" ref="scopes">
                        <a-select :default-value="addOrUpdateModel.scopes" @change="scopesChange" mode="tags" placeholder="请选择权限" style="width: 100%" :token-separators="[',']">
                            <a-select-option :key="scope.name.toLowerCase()" v-for="(scope) in scopes">
                                {{ scope.name.toLowerCase()}}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-item label="资源" name="resourceIds" ref="resourceIds">
                        <a-textarea  placeholder="请输入资源(逗号隔开)" :rows="6" v-model:value="addOrUpdateModel.resourceIds"/>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {Options,  Vue} from 'vue-property-decorator';
    import axios from "@lion/lion-frontend-core/src/network/axios";
    import { message } from 'ant-design-vue';
    @Options({
      components:{}
    })
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
                axios.get("/lion-oauth2-console-restful/client/console/check/clientId/exist",{params:{"clientId":this.addOrUpdateModel.clientId,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data) {
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
                        axios.put("/lion-oauth2-console-restful/client/console/update",this.addOrUpdateModel)
                            .then((data) =>{
                                if (Object(data).status === 200){
                                    message.success(Object(data).message);
                                    this.success();
                                }
                            }).catch((fail)=>{
                        }).finally(()=>{
                        })
                    }else {
                        axios.post("/lion-oauth2-console-restful/client/console/add",this.addOrUpdateModel)
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
            await axios.get("/lion-oauth2-console-restful/client/console/details",{params:{"id":id}})
            .then((data)=>{
                if (Object(data).status === 200 && data.data){
                    this.addOrUpdateModel=data.data;
                    this.addOrUpdateModel.scopes=data.data.scope.split(',');
                    this.addOrUpdateModel.grantTypes=data.data.authorizedGrantTypes.split(',');
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
            await axios.get("/lion-common-console-restful/enum/console/to/select", {params: {"enumClass": "com.lion.resource.enums.Scope"}})
            .then((data) => {
                this.scopes = data.data;
            })
            .catch(fail => {
            })
            .finally(() => {
            });
            await axios.get("/lion-common-console-restful/enum/console/to/select", {params: {"enumClass": "com.lion.resource.enums.GrantTypes"}})
            .then((data) => {
                this.authorizedGrantTypes = data.data;
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
       * 关闭弹窗时清空数据，以免数据污染
       * @private
       */
      private cancel():void {
        (this.$refs.addOrUpdateForm as any).clearValidate();
        (this.$refs.addOrUpdateForm as any).resetFields();
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