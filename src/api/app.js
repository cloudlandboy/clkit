import axios from "./axios";

function url(path) {
    return `/app/${path || ''}`;
}

export function getVersion() {
    return axios.get(url('version'));
}

export function checkUpdate() {
    return axios.get(url('check_update'));
}

export function update(updateServer, updateUi) {
    return axios.post(url('update'), { updateServer, updateUi }, {
        timeout: 1000 * 60 * 5
    });
}