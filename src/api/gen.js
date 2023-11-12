import axios from "./axios";

function url(path) {
    return `/gen/${path || ''}`;
}

export function genCrud(data) {
    return axios.post(url('crud'), data, {
        responseType: 'blob'
    });
}

export function saveTemplate(data) {
    return axios.post(url('template'), data);
}

export function updateTemplate(id, data) {
    return axios.put(url(`template/${id}`), data);
}

export function removeTemplate(id) {
    return axios.delete(url(`template/${id}`));
}

export function findTemplate(params) {
    return axios.get(url(`template`), { params });
}

export function unlockTemplate(id) {
    return axios.put(url(`template/unlock/${id}`));
}