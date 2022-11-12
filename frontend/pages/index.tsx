import Head from "next/head";
import {T} from "@tolgee/react";
import {Header} from "../components/Header/Header";
import {Container} from "../components/Container/Container";
import {Captcha} from "../components/Captcha/Captcha";
import {BirthdateDropdown} from "../components/BirthdateDropdown/BirthdateDropdown";
import {useState} from "react";
import {AcceptCheckbox} from "../components/AcceptCheckbox/AcceptCheckbox";
import {Footer} from "../components/Footer/Footer";
import {createApiClient} from "../api/api";
import {AxiosError} from "axios";
import {Alert, Title} from "@zendeskgarden/react-notifications";
import {PublicDecryptedDataResponseDTO} from "../api/types/PublicDecryptedDataResponseDTO";
import {CertificateView} from "../components/CertificateView/CertificateView";

interface ErrorObject {
    code: string;
    message: string;
    statusCode: number;
}

const Home = () => {

    const [accepted, setAccepted] = useState(true);
    const [birthdate, setBirthdate] = useState("");
    const [captcha, setCaptcha] = useState("");
    const [resetTimestamp, setResetTimestamp] = useState(0);

    const [error, setError] = useState<ErrorObject | undefined>(undefined);
    const [data, setData] = useState<PublicDecryptedDataResponseDTO | undefined>(undefined)

    const apiClient = createApiClient()
    const submitForm = () => {
        apiClient.getTestResult(birthdate, captcha).then((result) => {
            setData(result.data)
        }).catch((error: AxiosError) => {
            setError({
                code: error.response?.data.code,
                message: error.response?.data.message,
                statusCode: error.response?.status || 500
            })
            setResetTimestamp(Date.now())
        })
    }


    const birthDateForm = () => {
        return (<form onSubmit={
            (e) => {
                setError(undefined)
                submitForm();
                e.preventDefault()
            }
        }>
            <div className={"pt-12"}>
                <h2 className="text-2xl font-bold leading-6 text-gray-900">
                    <T>BIRTHDATE</T>
                </h2>
                <p className="mt-1 text-sm text-gray-500 pb-8">
                    <T>BIRTHDATE_INPUT_DESCRIPTION</T>
                </p>
                <BirthdateDropdown onChange={(date) => setBirthdate(date)}/>
                <AcceptCheckbox checked={accepted} onChange={setAccepted}/>
                <Captcha onCaptchaValid={setCaptcha} resetTimestamp={resetTimestamp}/>
            </div>
            <button
                type={"submit"}
                className="bg-primary-500 text-white w-full py-4 border rounded-3xl mt-8 hover:bg-primary-600 font-bold">
                <T>SUBMIT_BUTTON_LABEL</T>
            </button>
        </form>)
    }

    return (
        <div className="bg-gray-100 h-screen">
            <Head>
                <title>TestResult</title>
                <meta name="description" content="Generated by create next app"/>
                <link rel="icon" href="/favicon.ico"/>
            </Head>

            <main>
                <Header/>
                <Container>
                    {error && <Alert className={"mt-8"} type={"error"}>
                        <Title><T>{error.statusCode.toString()}</T></Title>
                        <T>{error.code}</T>
                    </Alert>
                    }
                    {data ? <CertificateView data={data} birthDate={birthdate}/> : birthDateForm()}
                </Container>
            </main>
            <Footer/>
        </div>
    )
}
export default Home;
