import '../styles/globals.css'
import type {AppProps} from 'next/app'
import {useRouter} from "next/router";
import {TolgeeProvider} from "@tolgee/react";
import {useEffect, useState} from "react";

const App = ({Component, pageProps}: AppProps) => {

    const router = useRouter();
    useEffect(() => {
        if (router.query.accessToken) {
            localStorage.setItem("accessToken", router.query.accessToken as string);
            router.replace(router.pathname, router.pathname, {shallow: true}).then(r => console.log(r));
        }
    }, [router.query])


    const {locale: activeLocale} = useRouter();
    return <TolgeeProvider
        forceLanguage={activeLocale || "de-DE"}
        apiKey={process.env.NEXT_PUBLIC_TOLGEE_API_KEY}
        apiUrl={process.env.NEXT_PUBLIC_TOLGEE_API_URL}
    >
        <Component {...pageProps} />
    </TolgeeProvider>
}
export default App