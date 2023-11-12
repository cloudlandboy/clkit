import axios from "./axios";

function url(path) {
    return `/office/${path}`;
}

export const convertUrl = `${axios.defaults.baseURL}${url('convert')}`;

export function listSupportFileType() {
    return axios.get(url('list_support_file_type'));
}

export function convert(file, data) {
    const form = new FormData();
    form.append('file', file);
    for (let key in data) {
        form.append(key, data[key]);
    }
    return axios.post(url('convert'), form, {
        responseType: 'blob'
    })
}
