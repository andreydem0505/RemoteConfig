FROM grafana/k6:1.4.0-with-browser

COPY . /scripts
WORKDIR /scripts

ENTRYPOINT ["k6"]
CMD ["run", "/scripts/script.js"]
# CMD ["run", "--out", "experimental-prometheus-rw", "/scripts/script.js"]
