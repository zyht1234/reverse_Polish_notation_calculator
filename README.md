#### 用途
基于RPN的计算器，可持续输入，后续输入在上次输入的计算结果上进一步计算
1、支持的操作符包括 + - * / sqrt clear undo,其中sqrt为开平方根，clear清除之前的所有计算结果，undo表示取消上一次操作（上一次操作不仅仅指加减乘除开方等运算，还包括数值的读取，clear操作等）
2、计算器兼容处理操作符对应的参数不够以及非法参数（除数不能为零，负数不能开实数平方根）等异常情况，出现异常时计算器程序不会死掉，输入clear后再输入表达式又可正常计算
3、手动关闭计算器时输入 end 即可

#### 打包
mvn clean package

#### 运行命令
进入target目录下运行命令：
java -jar reverse_polish_notation_calculator-1.0-SNAPSHOT.jar

#### 环境变量
PLACES_OF_STORE_PRECISION: 计算时采用的精度：十进制小数位数，默认为16
PLACES_OF_DISPLAY_PRECISION: 展示时采用的的精度:十进制小数位数，默认为10

#### 容器化部署
进入工程目录下运行命令,在本地就build好当前逆波兰的计算器的docker镜像
./ build_docker.sh

