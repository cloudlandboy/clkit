
import axios from "axios";
export function microsoftTranslator(key, data) {
    return axios.post('https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=en', data, {
        headers: {
            'Ocp-Apim-Subscription-Key': '5f787f5c06674a89851b00664248492c'
        }
    })
}