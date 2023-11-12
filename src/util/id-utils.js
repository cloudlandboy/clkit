export class AutoIncrementKey {
    step;
    value;
    constructor(initialValue, step) {
        if ((typeof initialValue !== 'number') || initialValue < 0) {
            initialValue = 0;
        }
        if ((typeof step !== 'number') || step < 1) {
            step = 1;
        }
        this.value = initialValue;
        this.step = step;
    }

    getKey() {
        this.value += this.step;
        return this.value;
    }

    getStringKey() {
        return this.getKey() + '';
    }
}