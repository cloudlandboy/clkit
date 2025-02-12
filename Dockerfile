FROM adoptopenjdk/openjdk8:jre8u422-b05

MAINTAINER syl@clboy.cn

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m"

RUN mkdir -p /root/.clkit

WORKDIR  /clkit

EXPOSE 28288

ADD ./target/clkit.jar ./

CMD java $JAVA_OPTS -jar clkit.jar
