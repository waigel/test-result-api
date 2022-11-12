import {T, useTranslate} from "@tolgee/react";
import {PublicDecryptedDataResponseDTO} from "../../api/types/PublicDecryptedDataResponseDTO";
import moment from "moment";
import {createApiClient} from "../../api/api";
import {useState} from "react";
import {TestResultColor} from "../../helpers/TestResultColor";
import {Anchor} from "@zendeskgarden/react-buttons";


export interface CertificateViewProps {
    data: PublicDecryptedDataResponseDTO;
    birthDate: string;
}

export const CertificateView = ({data, birthDate}: CertificateViewProps) => {
    const t = useTranslate();

    const [loading, setLoading] = useState(false);

    const apiClient = createApiClient()
    const downloadCertificate = () => {
        setLoading(true)
        apiClient.downloadCertificate(birthDate).then((result) => {
            const blob = new Blob([result.data], {type: 'application/pdf'});
            const url = URL.createObjectURL(blob)
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', t("FILENAME", {
                testId: data.testId,
                firstName: data.firstName,
                lastName: data.lastName,
            }).split(".pdf")[0] + ".pdf");
            document.body.appendChild(link);
            link.click();
        }).finally(() => {
            setLoading(false)
        })

    }

    return (
        <div className={"flex flex-col"}>

            <h2 className="text-3xl font-bold text-center pt-8">
                <T>CERTIFICATE</T>
            </h2>
            <p className={"text-center pt-2"}><T parameters={{
                date: moment(data.testPerformedAt).add(24, "hours").format("DD.MM.YYYY, HH:mm")
            }}>CERTIFICATE_EXPIRED_NOTICE</T>
            </p>

            <div className={"w-full bg-black my-8"} style={{height: "1px"}}/>
            <table className={"table-auto"}>
                <tbody>
                <tr>
                    <td className={"font-bold text-left"}><T>NAME</T></td>
                    <td>{data.firstName} {data.lastName}</td>
                </tr>
                <tr>
                    <td className={"font-bold text-left"}><T>TEST_ID</T></td>
                    <td>{data.testId}</td>
                </tr>
                <tr>
                    <td className={"font-bold text-left"}><T>TEST_PERFORMED_AT</T></td>
                    <td>{moment(data.testPerformedAt).format("DD.MM.YYYY, HH:mm")} {t("TIME_SUFFIX")}</td>
                </tr>
                <tr>
                    <td className={"font-bold text-left"}><T>TEST_TYPE</T></td>
                    <td>{data.testName}</td>
                </tr>
                <tr>
                    <td className={"font-bold text-left"}><T>TEST_RESULT</T></td>
                    <td style={{
                        color: TestResultColor[data.testResult]
                    }}><T>{data.testResult}</T></td>
                </tr>
                <tr>
                    <td className={"font-bold text-left"}><T>DOWNLOAD_CERTIFICATE</T></td>
                    <td>
                        <br/>
                        <button
                            disabled={loading}
                            onClick={downloadCertificate}
                            className="bg-primary-500 text-white px-8 py-2 border rounded-3xl hover:bg-primary-600 font-bold">
                            {loading ? <T>DOWNLOADING</T> : <T>DOWNLOAD</T>}
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
            {(data.cwaQrCode && data.cwaAppLink) && (
                <>
                    <div className={"w-full bg-black my-8"} style={{height: "1px"}}/>
                    <div className={"flex flex-col w-full justify-center text-center"}>
                        <h2 className="text-xl font-bold">
                            <T>CWA</T>
                        </h2>
                        <p><T>CWA_DESCRIPTION</T></p>
                        <br/>
                        <img className={"mx-auto"} width={"230rem"} src={data.cwaQrCode} alt={"CWA QR Code"}/>
                        <small><T>HINT_APP_LINK_LINE_1</T></small>
                        <small><T>HINT_APP_LINK_LINE_2</T></small>
                        <Anchor href={data.cwaAppLink} target={"_blank"} rel="noreferrer">
                            <T>OPEN_CWA_APP_LINK</T>
                        </Anchor>
                    </div>
                    <div className={"w-full bg-black my-8"} style={{height: "1px"}}/>
                </>
            )}
        </div>)
}