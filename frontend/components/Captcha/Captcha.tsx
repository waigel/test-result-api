import {Turnstile} from "@marsidev/react-turnstile";

export const Captcha = () => {
    return <Turnstile siteKey={process.env.NEXT_PUBLIC_CLOUDFLARE_TURNSTILE_PUBLIC_SITE_KEY}
                      className='static md:fixed bottom-4 right-4 z-50 mx-7 md:mx-0 pt-4'
                      options={{
                          action: 'submit-form',
                          theme: 'light',
                          size: 'normal'
                      }}
                      scriptOptions={{
                          appendTo: 'body'
                      }}/>
}