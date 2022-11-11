import axios, {AxiosResponse} from "axios";
import {PublicDecryptedDataResponseDTO} from "./types/PublicDecryptedDataResponseDTO";

export const createApiClient = () =>{
    return {
        getTestResult: async (birthDate: string): Promise<AxiosResponse<PublicDecryptedDataResponseDTO>> => {
            return await axios.post(process.env.NEXT_PUBLIC_API_URL + '/api/v1/public/decrypt-data', {
                birthDate
            }, {
                headers: {
                    'Content-Type': 'application/json',
                    'X-Access-Token': localStorage.getItem('accessToken')
                } as any

            })
        },
        downloadCertificate(birthDate: String) {
            return axios.post(process.env.NEXT_PUBLIC_API_URL + '/api/v1/public/decrypt-data/certificate', {
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