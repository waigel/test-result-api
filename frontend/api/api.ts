import axios, {AxiosResponse} from "axios";
import {PublicDecryptedDataResponseDTO} from "./types/PublicDecryptedDataResponseDTO";

export const createApiClient = (baseUrl: string) =>{
    return {
        getTestResult: async (birthDate: string, captcha: string): Promise<AxiosResponse<PublicDecryptedDataResponseDTO>> => {
            return await axios.post(baseUrl + '/api/v1/public/decrypt-data', {
                birthDate
            }, {
                headers: {
                    'Content-Type': 'application/json',
                    'X-Access-Token': localStorage.getItem('accessToken'),
                    'X-Captcha-Token': captcha

                } as any

            })
        },
        downloadCertificate(birthDate: String) {
            return axios.post(baseUrl + '/api/v1/public/decrypt-data/certificate', {
                birthDate
            }, {
                headers: {
                    'Content-Type': 'application/json',
                    'X-Access-Token': localStorage.getItem('accessToken')
                } as any,
                responseType: 'blob'
            })

        }
    }
}