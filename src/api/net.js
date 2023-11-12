import axios from "./axios";


function url(path) {
    return `/net/${path}`;
}

export function getLanIp() {
    return axios.get(url('lan_ip'));
}

export function scanLanIp(query) {
    return axios.get(url('scan_lan_ip'), { params: query });
}

export function findPid(type, value) {
    return axios.get(url('find_pid'), { params: { type, value } });
}

export function killPort(body) {
    return axios.post(url('kill_port'), body);
}

export function killPid(body) {
    return axios.post(url('kill_pid'), body);
}