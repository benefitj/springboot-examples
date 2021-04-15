## 时需数据库PostgreSQL/Timescale

创建容器：

    docker run -d --restart=always \
     --network mynet --network-alias timescaledb-net \
     -v /etc/localtime:/etc/localtime   \
     -v /etc/timezone:/etc/timezone   \
     -v /usr/bin/docker:/usr/bin/docker \
     -v /var/run/docker.sock:/var/run/docker.sock \
     -p 5432:5432 \
     -e POSTGRES_PASSWORD=123456 \
     -v /home/programs/timescaledb/data:/var/lib/postgresql/data \
     --name timescaledb \
     timescale/timescaledb

