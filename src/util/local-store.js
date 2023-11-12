class LocalStore {
    prefix;
    prefixDot;
    constructor(prefix) {
        this.prefix = prefix;
        this.prefixDot = prefix ? prefix + '.' : '';
    }

    getOrDefault(key, defaultValue, convertFunc) {
        const value = localStorage.getItem(this.key(key));
        if (!value || value.trim().length < 1) {
            return defaultValue;
        }
        return convertFunc ? convertFunc(value) : value;
    }

    getJsonOrDefault(key, defaultValue) {
        return this.getOrDefault(key, defaultValue, v => JSON.parse(v));
    }

    getJsonOrEmpty(key) {
        return this.getJsonOrDefault(key, {});
    }

    getJsonKeyOrDefault(key, jsonKey, defaultValue) {
        return this.getJsonOrEmpty(key)[jsonKey] || defaultValue;
    }

    getNumberOrDefault(key, defaultValue) {
        return this.getOrDefault(key, defaultValue, v => Number(v))
    }

    getBoolean(key) {
        return Boolean(localStorage.getItem(this.key(key)));
    }

    set(key, value) {
        key = this.key(key);
        const type = (typeof value)
        if (!type || type === 'function') {
            return
        } else if (type === 'object') {
            localStorage.setItem(key, JSON.stringify(value));
        } else {
            localStorage.setItem(key, value + '');
        }
    }

    key(k) {
        return this.prefixDot + k;
    }
}

const localStore = new LocalStore('');

export function prefixLocalStore(prefix) {
    return new LocalStore(prefix);
}

export default localStore;