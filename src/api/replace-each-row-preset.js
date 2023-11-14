import axios from "./axios";

function url(path) {
    return `/replace_each_row/${path || ''}`;
}

export function create(data) {
    return axios.post(url(), data);
}

export function findAll() {
    return axios.get(url());
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