import React from 'react';
import {Anchor} from '@zendeskgarden/react-buttons';
import {useTranslate} from "@tolgee/react";

export const Footer = () => {
    const currentYear = new Date().getFullYear()
    const t = useTranslate();

    const frontendVersion = () => {
        if (process.env.NEXT_PUBLIC_BRANCH) {
            return `${
                process.env.NEXT_PUBLIC_BRANCH
            }/${process.env.NEXT_PUBLIC_COMMIT_SHA?.match(/.{7}/g)?.at(0)}`;
        }
        return 'dev';
    };

    return (
        <div className={"flex pb-8 text-center bottom-0 left-0 right-0"} style={{
            marginTop: "auto",
        }}>
            <div className={"m-auto flex flex-col"}>
                <div className={"flex flex-row gap-2 m-auto"}>
                    <Anchor href={t("LINK_PRIVACY")} target="_blank"> {t("LABEL_PRIVACY")}</Anchor>
                    <samp>|</samp>
                    <Anchor href={t("LINK_ABOUT_US")} target="_blank"> {t("LABEL_ABOUT_US")}</Anchor>
                </div>
                <p>© {currentYear} Test Perfect by
                    <Anchor href="https://novax-digital.de/" target="_blank">
                        {" "}Novax Digital GmbH
                    </Anchor>
                </p>
                <small className={"text-xs text-gray-400 py-2"}>{frontendVersion()}</small>
            </div>
        </div>
    );
};
