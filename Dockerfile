FROM python:3.10

RUN apt-get update && \
    apt-get install -y python3-pip gnupg software-properties-common wget lsb-release

ENV TZ=Asia/Seoul

RUN wget -O- https://apt.releases.hashicorp.com/gpg | gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] \
    https://apt.releases.hashicorp.com $(lsb_release -cs) main" | \
    tee /etc/apt/sources.list.d/hashicorp.list

RUN apt-get update && \
    apt-get install -y tzdata terraform

WORKDIR /home

COPY . .

RUN pip install --upgrade pip && \ 
    pip install -r requirements.txt

ENTRYPOINT ["python3", "app.py"]