export function hasText(str) {
    return (typeof str === 'string') && str.trim().length > 0
}


export function placeholderReplace(text, context) {
    for (const key in context) {
        text = text.replace(new RegExp(`\\$\\{${key}\\}`, 'g'), context[key]);
    }
    return text;
}