<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <!-- import CSS -->
    <!-- <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">

     <script src="https://cdn.staticfile.org/vue/2.2.2/vue.min.js"></script>
     &lt;!&ndash; import JavaScript &ndash;&gt;
     <script src="https://unpkg.com/element-ui/lib/index.js"></script>-->
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <!-- import Vue before Element -->
    <script src="https://unpkg.com/vue/dist/vue.js"></script>
    <!-- import JavaScript -->
    <script src="https://unpkg.com/element-ui/lib/index.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>


</head>
<body>
<div id="app">
    <demo5></demo5>
</div>
<script>
    Vue.component(
            'demo5',
            {
                template: '  <el-collapse v-model="activeNames" @change="handleChange">\n' +
                '        <el-collapse-item v-for=" column in TableMsgs.TableNames" :title="column.name" :name="column.name">\n' +
                '            <el-tabs v-model="activeName" @tab-click="handleClick">\n' +
                '                <el-table\n' +
                '                        :data=column.Message\n' +
                '                        border\n' +
                '                        style="width:100%">\n' +
                '                    <el-table-column v-for="columns in column.Propertynames"\n' +
                '                                     fixed\n' +
                '                                     :prop="columns.prtysName"\n' +
                '                                     :label="columns.prtysName"\n' +
                '                                     width="60">\n' +
                '                    </el-table-column>\n' +
                '                    <el-table-column\n' +
                '                            fixed="right"\n' +
                '                            label="操作"\n' +
                '                            width="160">\n' +
                '                        <template slot-scope="scope">\n' +
                '                            <el-button @click="handleClick(scope.row)" type="text" size="small"></el-button>\n' +
                '                            <el-button\n' +
                '                                    @click.native.prevent="DeleteRow(scope.$index,column.Message,column.name,column.Message[scope.$index].id)"\n' +
                '                                    type="text"\n' +
                '                                    size="small">\n' +
                '                                移除\n' +
                '                            </el-button><!--删除-->\n' +
                '                            <el-popover\n' +
                '                                    placement="right"\n' +
                '                                    width="400"\n' +
                '                                    trigger="click">\n' +
                '                                <p>请填写添加信息</p>\n' +
                '                               <label>输入{{column.freename}}：</label><el-input  :placeholder="column.freename.toString()" v-model="free"></el-input>\n' +
                '\n' +
                '                                <form v-for="prop in column.Propertynames">\n' +
                '                                    <label>输入{{prop.prtysName}}：</label><el-input  :placeholder="prop.prtysName.toString()" v-model="prop.prtysInstruct"></el-input>\n' +
                '                                </form>\n' +
                '                                <el-button\n' +
                '                                        type="primary"\n' +
                '                                        size="small"\n' +
                '                                        @click.native.prevent="AddMsg(column.Propertynames,column.name)">\n' +
                '                                    提交\n' +
                '                                </el-button>\n' +
                '                                <el-button type="text" size="small" slot="reference" @click="test(column.Propertynames,column.name)">添加</el-button>\n' +
                '\n' +
                '                            </el-popover><!--添加-->\n' +
                '                            <el-popover\n' +
                '                                    placement="right"\n' +
                '                                    width="400"\n' +
                '                                    trigger="click">\n' +
                '                                <p>请填写修改信息</p>\n' +
                '                                <label>输入{{column.freename}}：</label><el-input  :placeholder="column.freename.toString()" v-model="free"></el-input>\n' +
                '\n' +
                '                                <form v-for="prop in column.Propertynames">\n' +
                '                                    <label>输入{{prop.prtysName}}：</label><el-input  :placeholder="prop.prtysName.toString()" v-model="prop.prtysInstruct"></el-input>\n' +
                '                                </form>\n' +
                '                                <el-button type="text" size="small" slot="reference" @click="test(column.Propertynames,column.name)">修改</el-button>\n' +
                '                                <el-button\n' +
                '                                        type="primary"\n' +
                '                                        size="small"\n' +
                '                                        @click.native.prevent="UpDataMsg(column.Propertynames,column.name,column.Message[scope.$index].id)">\n' +
                '                                    提交\n' +
                '                                </el-button>\n' +
                '                            </el-popover><!--修改-->\n' +
                '                        </template>\n' +
                '\n' +
                '                    </el-table-column>\n' +
                '                </el-table>\n' +
                '            </el-tabs>\n' +
                '\n' +
                '        </el-collapse-item>\n' +
                '    </el-collapse>',

                data() {
                    return {
                        add: "默认的add值",
                        activeNames: "first",
                        activeName: 'first',
                        TableMsgs: {
                            TableNames: [
                                {
                                    Propertynames: [
                                        {
                                            addmsg: '',
                                            updatamsg: '',
                                            prtysName: 'name',
                                            prtysInstruct: '说明',
                                        },
                                        {
                                            add: '',
                                            prtysName: 'id',
                                            updatamsg: '',
                                            prtysInstruct: '说明',
                                        }
                                    ],
                                    Message: [
                                        {
                                            name: 'wangbin',
                                            id: '1',
                                            age: '100'
                                        },
                                        {
                                            name: 'wangbin2',
                                            id: '2',
                                        },
                                        {
                                            name: 'wangbin3',
                                            id: '3',
                                        },
                                        {
                                            name: 'wangbin4',
                                            id: '4',
                                        },
                                    ],
                                    name: '这是默认的表名',///存放表明
                                    id: 0,
                                    projectName: '',
                                    freename: '',
                                    names: '',

                                },
                            ],
                            Allfotablename: [
                                {
                                    name: '',
                                    id: 0,
                                    projectName: ''
                                }
                            ],
                            Allofprops: [
                                {
                                    id: '',
                                    name: ''
                                }
                            ],
                            //这里可以存放表的属性和信息
                        },
                        Addinfo: [],//拿到需要添加对象的集合
                        posts: {},//吧集合的第0个对像赋值给这个对象
                        initobj: [],
                        free: ''
                    };
                },
                methods: {
                    test(getlength, gettablename) {
                        this.add = gettablename;
                        for (let info = 0; info < getlength.length; info++) {
                            getlength[info].prtysInstruct = '请输入数据';
                        }
                    },
                    handleChange(val) {
                        console.log(val);
                    },
                    handleClick(row) {
                        console.log(row);
                    },
                    tablelist() {
                        let that = this;
                        let url = "http://202.196.37.147:8848/TableMessage/list";
                        axios.get(url, {}).then(function (response) {
                            for (let j = 0; j < response.data.length; j++) {
                                let yuan = response.data[j].tablename;
                                let first = response.data[j].tablename.toString().substr(0, 1).toUpperCase();
                                let last = response.data[j].tablename.toString().substr(1, response.data[j].tablename.toString().length);
                                let now = first + last;
                                that.TableMsgs.TableNames.push
                                ({
                                    names: yuan,
                                    name: now,
                                    id: response.data[j].id,
                                });
                                /* console.log("看看表明"+that.TableMsgs.TableNames[j+1].name);*/
                                that.TableMsgs.TableNames[j + 1].Message = [];//因为在这里把数组初始化清空所以不能显示原有定义的数据
                                that.TableMsgs.TableNames[j + 1].Propertynames = [];//初始化数组建立


                            }
                            that.$nextTick(() = > {

                                for(let i = 1;
                            i < that.TableMsgs.TableNames.length;
                            i++
                        )
                            {
                                axios({
                                    method: 'post',
                                    url: 'http://202.196.37.147:8848/TableMessage/findMyProps',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    // withCredentials: true,
                                    params: {
                                        TableMessageName: that.TableMsgs.TableNames[i].name
                                    },
                                }).then(function (res) {
                                    console.log("属性接收");
                                    /*  for (let j in res.data )
                                      {
                                          let first=res.data[j].prtysName.toString().substr(0,1).toUpperCase();
                                          let last=res.data[j].prtysName.toString().substr(1,res.data[j].prtysName.toString().length);
                                          let now=first+last;
                                          res.data[j].prtysName=now;
                                      }*/
                                    that.TableMsgs.TableNames[i].Propertynames = res.data;
                                    that.getmsg();
                                });
                            }

                        })
                            ;


                        }).catch(function (error) {
                            console.log(error);
                        });
                        console.log("输出所有函数调用完成的表数组长度:" + that.TableMsgs.TableNames.length);
                    },
                    PropertyFindall() {
                        let that = this;
                        let url = 'http://localhost:8848/Pros/list';
                        axios(
                                {
                                    method: 'get',
                                    url,
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    // withCredentials: true,
                                }
                        ).then(function (res) {
                            for (let i = 0; i < res.data.length; i++) {
                                for (let j = 0; j < that.TableMsgs.TableNames.length; j++) {
                                    if (that.TableMsgs.TableNames[j].id == res.data[i].id) {
                                        that.TableMsgs.TableNames[j].Propertynames.push
                                        (
                                                {
                                                    Proname: res.data[i].prtysName,
                                                    add: '属性添加',
                                                    updatamsg: '属性更新',
                                                }
                                        );
                                        //在这里查询值给数组message，里面
                                    }
                                }
                                that.TableMsgs.Allofprops.push(
                                        {
                                            name: res.data[i].prtysName,
                                            //  id:res.data[i].tableMessage_ID,
                                        }
                                )
                                // console.log(res.data[i].id + '属性名idididiid');
                            }
                            //"得到数据
                        });
                    },
                    DeleteRow(index, rows, gettablename, id) {
                        console.log("输出index" + index);
                        console.log("输出id" + id);
                        let that = this;

                        rows.splice(index, 1);

                        for (let p in this.TableMsgs.TableNames[4].Message[3]) {
                            console.log("输出id" + p);
                        }

                        let url = "http://localhost:8848/" + gettablename + "/deleteById";
                        axios(
                                {
                                    method: 'post',
                                    url,
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    //withCredentials:true,
                                    params: {
                                        Id: id
                                    }

                                }
                        );
                        this.$alert("删除成功");
                    },
                    UpDataMsg(getlength, gettablename, id) {
                        let that = this;
                        this.add = gettablename;
                        this.Addinfo = getlength;
                        that.packinfo(gettablename, 2);
                        let flag = true;
                        for (let ad = 0; ad < this.Addinfo.length; ad++) {
                            if (this.Addinfo[ad].prtysInstruct == 'zut') {
                                flag = false;
                            } else {
                                console.log("这是添加的信息" + this.Addinfo[ad].prtysInstruct);
                            }
                        }
                        if (flag == false) {
                            this.$alert("请填写全部信息");
                        } else {
                            let url = "http://localhost:8848/" + gettablename + "/updataById";
                            let obj = that.posts;
                            this.$set(obj, "id", id);
                            console.log(id + "这是updata的id");
                            /* for (let w in obj)
                             {
                                 console.log("遍历前："+w+":"+obj[w]);
                             }*/
                            axios({
                                method: 'post',
                                url,
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                // withCredentials: true,
                                params: {
                                    Id: id
                                },
                                data: obj,
                            }).then(function (res) {

                            });
                            /* for (let w in obj)
                             {
                                 console.log("遍历："+w+":"+obj[w]);
                             }*/
                            this.$alert("修改成功");
                        }
                    },
                    AddMsg(getlength, gettablename) {
                        //getlength这是获取这个表的所有属性的对像的集合
                        //在拿到的集合里面的每个对象的说明instruction输入信息在后赋值给清空的posts对象
                        let that = this;
                        let flag = true;
                        this.Addinfo = getlength; //吧集合赋值给这个中间定义的空集合
                        this.add = gettablename;
                        that.packinfo(gettablename, 1);
                        for (let ad = 0; ad < this.Addinfo.length; ad++) {
                            if (this.Addinfo[ad].prtysInstruct == 'zut') {
                                flag = false;
                            }

                        }

                        if (flag == false) {
                            this.$alert("请填写全部信息");
                        } else {
                            for (let info in that.posts) {
                                console.log("这是调用前遍历post:" + info + that.posts[info])
                            }
                            let url = "http://localhost:8848/" + gettablename + "/add";
                            axios({
                                method: 'post',
                                url,
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                //withCredentials: true,
                                data: that.posts,

                                /*  data:{
                                      studentage:'12312312',
                                      studentName:'王斌2'
                                  }*/
                            }).then(function (res) {

                            });
                            this.$alert("添加成功");
                        }
                    },
                    packinfo(gettablename, num) {
                        let that = this;
                        let ad = 0;
                        let addname = '';
                        for (let info in  that.posts) {
                            info = "";
                        }//把对象的属性值成为空
                        for (let i = 0; i < that.TableMsgs.TableNames.length; i++) {
                            if (gettablename === that.TableMsgs.TableNames[i].name) {
                                that.posts = this.initobj[i];
                                addname = that.TableMsgs.TableNames[i].freename;
                            }
                        }
                        /* for (let info in  that.posts)
                         {
                             console.log("这是遍历对象posts"+info );
                         }*/
                        /* for (let i=0;i< that.Addinfo.length;i++)
                         {
                             console.log("遍历说明i:"+i+  that.Addinfo[i].prtysInstruct);
                         }
              */
                        /*                console.log("that.Addinfo长度:"+  that.Addinfo.length);*/
                        for (let info in  that.posts) {
                            if (ad < this.Addinfo.length) {
                                console.log("赋值" + that.Addinfo[ad].prtysInstruct);
                                that.posts[info] = that.Addinfo[ad].prtysInstruct;
                                ad++;
                            }
                        }
                        that.posts[addname] = this.free;
                        console.log("freed的值" + this.free);
                        /*   for (let info in  that.posts)
                           {
                               console.log("后这是遍历对象posts:"+info+that.posts[info] );
                           }*/
                    },
                    pushdata() {
                        console.log("调用pushdata");
                        let that = this;
                        let freename;
                        /*  console.log("输出tablename长度"+this.TableMsgs.TableNames.length);*/
                        for (let i = 0; i < this.TableMsgs.TableNames.length; i++) {
                            this.TableMsgs.TableNames[i].freename = this.TableMsgs.TableNames[i].names + "Name";
                            this.initobj[i] = {};
                            if (this.TableMsgs.TableNames[i].Propertynames.length != 0) {/* console.log("输出pushdata看名字"+i+ this.TableMsgs.TableNames[i].name);*/
                                for (let j = 0; j < this.TableMsgs.TableNames[i].Propertynames.length; j++) {
                                    that.$set(that.initobj[i], that.TableMsgs.TableNames[i].Propertynames[j].prtysName, '操作');
                                }
                                this.$set(that.initobj[i], that.TableMsgs.TableNames[i].freename, '操作');
                                this.TableMsgs.TableNames[i].Message[0] = this.initobj[i];
                                /*  for (let w in  this.TableMsgs.TableNames[i].Message[0])
                                  {
                                      console.log("输出表:i"+i+":"+this.TableMsgs.TableNames[i].name+"的遍历结果:"+w);
                                  }*/

                            }
                        }
                    },
                    getmsg() {

                        console.log("调用了getmsg()");
                        let that = this;
                        for (let i = 1; i < this.TableMsgs.TableNames.length; i++) {
                            let url = "http://localhost:8848/" + this.TableMsgs.TableNames[i].name + "/list";
                            axios.get(url, {}).then(function (response) {
                                if (response.data.length != 0) {
                                    that.TableMsgs.TableNames[i].Message = response.data;
                                }

                            });

                        }
                        that.pushdata();
                    }
                },
                created() {
                    this.tablelist();
                }
            }
    );
</script>
</body>
<script>
    new Vue({
        el: '#app',
    })
</script>
</html>
