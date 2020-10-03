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
                    <a-form-model-item label="权限" prop="scope" ref="scope">
                        <a-select v-model="addOrUpdateModel.scope" mode="tags" style="width: 100%" :token-separators="[',']" @change="scopeChange">
                            <a-select-option v-for="i in 25" :key="(i + 9).toString(36) + i">
                                {{ (i + 9).toString(36) + i }}
                            </a-select-option>
                        </a-select>
                    </a-form-model-item>
                </a-col>
            </a-row>
            <a-row>
                <a-col :span="24">
                    <a-form-model-item label="资源" prop="resourceIds" ref="resourceIds">
                        <a-input placeholder="请输入资源" v-model="addOrUpdateModel.resourceIds" />
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
        private addOrUpdateModel:any={}
        //校验规则
        private rules:any={
            clientId:[{required:true,validator:this.checkCodeIsExist,trigger:'blur'}],
            clientSecretPlaintext:[{required:true,trigger:'blur'}],
            scope:[{required:true,trigger:'blur'}],
        };

        /**
         * 检查编码是否存在
         * @param rule
         * @param value
         * @param callback
         */
        private checkCodeIsExist(rule :any, value:string, callback:any):void{
            if (!value || value.trim() === ''){
                callback(new Error('请输入编码'));
                return;
            }else if (value && value.trim() !== ''){
                axios.get("/common/parameter/console/check/code/exist",{params:{"code":this.addOrUpdateModel.code,"id":this.addOrUpdateModel.id}})
                    .then((data)=> {
                        if (Object(data).status !== 200){
                            callback(new Error('异常错误！请检查'));
                            return;
                        }
                        if (data.data.isExist) {
                            callback(new Error('编码已存在'));
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
                    if (this.addOrUpdateModel.id){
                        axios.put("/common/parameter/console/update",this.addOrUpdateModel)
                            .then((data) =>{
                                if (Object(data).status === 200){
                                    message.success(Object(data).message);
                                    this.success();
                                }
                            }).catch((fail)=>{
                        }).finally(()=>{
                        })
                    }else {
                        axios.post("/common/parameter/console/add",this.addOrUpdateModel)
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
        private getDetails(id:string):void{
            axios.get("/common/parameter/console/details",{params:{"id":id}})
                .then((data)=>{
                    if (Object(data).status === 200 && data.data.parameter){
                        let parameter = data.data.parameter;
                        this.addOrUpdateModel=parameter;
                        this.addOrUpdateModal=true;
                    }
                })
                .catch(fail => {
                })
                .finally(()=>{
                });
        }

        /**
         * 提交数据成功后事件
         */
        private success():void{
            this.addOrUpdateModal = false;
            this.addOrUpdateModel={};
            (this.$parent as any).search();
        }

    }
</script>

<style scoped>
    #remark >>> .ant-form-item-control-wrapper{
        width: calc(100% - 80px);
    }
    #remark >>> .ant-form-item{
        width: 100%;
    }
    .ant-form-item >>> .ant-form-item-label{
        width: 80px;
    }
    .ant-form-item{
        width: 100%;
    }
    .ant-row >>> .ant-form-item-control-wrapper{
        width: calc(100% - 80px);
    }
</style>