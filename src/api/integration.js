import axios from "./axios";

function url(path) {
    return `/integration/${path || ''}`;
}

export function create(data) {
    return axios.post(url(), data);
}

export function findAll() {
    return axios.get(url());
}

export function findAllInstalled() {
    return axios.get(url('installed'));
}

export function findById(id) {
    return axios.get(url(id));
}

export function update(id, data) {
    return axios.put(url(id), data);
}

export function remove(id) {
    return axios.delete(url(id));
}

export function install(id) {
    return axios.post(url(`install/${id}`));
}

export const types = {
    "onlineUrl": {
        label: '在线网址',
        needInstall: false
    },
    "downloadUrl": {
        label: '文件下载地址',
        needInstall: true
    },
    "diskPath": {
        label: '本地磁盘路径',
        needInstall: true
    }
};