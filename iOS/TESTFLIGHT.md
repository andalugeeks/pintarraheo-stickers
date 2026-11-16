# Pintarraheô Stickers para iOS: compilación en la nube y TestFlight

El proyecto iOS (`iOS/WAStickersThirdParty.xcodeproj`, la app de ejemplo de WhatsApp
adaptada) se compila sin Mac propio: el workflow
[`ios-testflight.yml`](../.github/workflows/ios-testflight.yml) corre en un runner
`macos-26` de GitHub Actions (Xcode 26.6), firma con la firma gestionada en la nube de
Xcode usando la API Key de App Store Connect y sube la build a TestFlight.

## Cuándo se ejecuta

- A mano: Actions → *iOS TestFlight* → *Run workflow* (cuando el workflow esté en `main`).
- En push a `main` o `ios-testflight` que toque `iOS/**` o el propio workflow.

## Datos de la app

- Bundle ID: `es.andaluh.pintarraheoStickers` (el mismo que en Android).
- Equipo: *AndaluGeeks Team*, Team ID `LAWPFLVDHJ`.
- Versión (`MARKETING_VERSION`): alineada a mano con `versionName` de `Android/app/build.gradle`.
- Número de build: `<run_number>.<run_attempt>` del workflow, siempre creciente.
- Stickers: copiados de `Android/app/src/main/assets/1/`; `sticker_packs.wasticker`
  generado a partir de `contents.json`. Si cambian los stickers en Android, hay que
  copiarlos aquí y actualizar `project.pbxproj`.

## Configuración en Apple (una sola vez)

- App creada en App Store Connect con el bundle ID de arriba (la API de Apple no permite
  crear apps; Xcode registra el App ID automáticamente con `-allowProvisioningUpdates`).
- TestFlight: grupo de **Pruebas internas** para probar sin Beta App Review.

## Secretos de GitHub

Los mismos que en `andalugeeks/paisaje-linguistico` (API Key de equipo, rol Admin):
`ASC_KEY_P8`, `ASC_KEY_ID`, `ASC_ISSUER_ID`.

## Aviso para publicar en el App Store

Apple suele rechazar apps que solo exportan stickers a WhatsApp (guideline 4.2, funcionalidad
mínima) y pide que la interfaz no sea la del ejemplo. TestFlight interno no pasa revisión,
pero antes de enviar a revisión habrá que añadir funcionalidad y personalizar la UI.
