FROM grafana/k6:1.3.0

COPY . /scripts
WORKDIR /scripts

ENTRYPOINT ["k6"]
CMD ["run", "/scripts/script.js"]
# CMD ["run", "--out", "experimental-prometheus-rw", "/scripts/script.js"]
