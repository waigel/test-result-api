import {Turnstile, TurnstileInstance} from "@marsidev/react-turnstile";
import {useEffect, useRef} from "react";

export interface CaptchaProps {
    onCaptchaValid: (value: string) => void;
    resetTimestamp?: number;
    cfTurnstilePublicSiteKey: string;
}

export const Captcha = ({onCaptchaValid, resetTimestamp, cfTurnstilePublicSiteKey}: CaptchaProps) => {
    const ref = useRef<TurnstileInstance | null>(null)

    useEffect(() => {
        if (ref.current) {
            ref.current?.reset()
        }
    }, [resetTimestamp])

    return <Turnstile ref={ref} siteKey={cfTurnstilePublicSiteKey}
                      className='static md:fixed bottom-4 right-4 z-50 mx-7 md:mx-0 pt-4'
                      options={{
                          action: 'submit-form',
                          theme: 'light',
                          size: 'normal'
                      }}
                      onSuccess={onCaptchaValid}
                      scriptOptions={{
                          appendTo: 'body'
                      }}/>
}