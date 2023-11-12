import axios from "./axios";

function url(path) {
    return `/app/${path || ''}`;
}

export function getVersion() {
    return axios.get(url('version'));
}