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

export function getTree() {
    return axios.get(url('tree'));
}

export function getInstalledTree() {
    return axios.get(url('installed_tree'));
}