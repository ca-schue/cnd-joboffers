import {LocalConfig} from "./LocalConfig";
import {OfflineConfig} from "./OfflineConfig";
import {ProdConfig} from "./ProdConfig";


export const config = getConfig();

function getConfig() {
    switch (process.env.APP_ENV) {
        case "offline":
            return OfflineConfig;
        case "local":
            return LocalConfig;
        case "prod":
            return ProdConfig;
        default:
            throw new Error(`Invalid APP_ENV "${process.env.APP_ENV}"`);
    }
}
