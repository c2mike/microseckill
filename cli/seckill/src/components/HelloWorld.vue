<template>
  <div >
    <el-table
            :data="tableData"
            border
            size="small"
            >
      <el-table-column
              prop="beginTime"
              label="开始日期"
              width="150">
      </el-table-column>
      <el-table-column
              prop="endTime"
              label="结束日期"
              width="150">
      </el-table-column>
      <el-table-column
              prop="sku.name"
              label="商品名称">
      </el-table-column>
      <el-table-column
              prop="quanty"
              label="数量">
      </el-table-column>
      <el-table-column
            prop="price"
            label="价格">
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button size="small" @click="buy(scope.row)">购买</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
    import { Message } from 'element-ui';
export default {
  name: 'HelloWorld',
    methods: {
        buy(data)
        {
            var phone = this.$store.state.phone;
            if(!phone)
            {
                Message.success("请先登录");
                return;
            }
            else
            {
                console.log(data);
                var sid = data.skuId;
                this.getRequest("/orderServer/md5?sid="+sid+"&phone="+phone).then(
                    data=>{
                        if(data)
                        {
                            var md5 = data.obj;
                            var object = new Object();
                            object.sid = sid;
                            object.phone = phone;
                            object.md5 = md5;
                            this.postKeyValueRequest("/orderServer/order",object).then(data=>{
                                this.initData();
                            })

                        }
                        else{

                        }
                    }
                )
            }
        },
        handleClick(row) {
            console.log(row);
        },
        initData()
        {
            this.getRequest("/stockServer/politicals").then(data=>{
                if(data.obj)
                {
                    this.tableData = data.obj;
                }
            })
        }
    },
    mounted(){
          this.initData();
    },
    data() {
        return {
            tableData: []
        }
    }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
