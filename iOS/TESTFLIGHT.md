# Pintarraheô Stickers para iOS: compilación en la nube y TestFlight

El proyecto iOS (`iOS/WAStickersThirdParty.xcodeproj`, la app de ejemplo de WhatsApp
adaptada) se compila sin Mac propio: el workflow
[`ios-testflight.yml`](../.github/workflows/ios-testflight.yml) corre en un runner
`macos-26-intel` de GitHub Actions (Xcode 26.6) y sube la build a TestFlight.

## Cuándo se ejecuta

- A mano: Actions → *iOS TestFlight* → *Run workflow*.
- En push a `main` que toque `iOS/**` o el propio workflow. Un commit vacío o que solo
  toque otras rutas **no** lo lanza.

## Firma

- El `archive` se hace **sin firmar** (`CODE_SIGNING_ALLOWED=NO`) y la firma solo se hace en
  `-exportArchive`, con el certificado de distribución gestionado en la nube y la API Key.
- Motivo: firmar el archive hacía que Xcode creara un certificado de desarrollo nuevo en
  cada runner, hasta llegar al límite del equipo (*Choose a certificate to revoke*). Los
  10 que se acumularon se revocaron el 2026-11-16.
- No hay certificados ni perfiles en el repo.

## Runner

- `macos-26-intel` en lugar de `macos-26` (arm64): en nov-2026 el pool arm64 no tenía
  capacidad y los jobs se cancelaban a los 15 min sin runner (*The job was not acquired
  by Runner of type hosted*). La imagen Intel trae el mismo Xcode.

## Datos de la app

- App Store Connect: "Pintarraheo Stickers", Apple ID `6835412982`.
- Bundle ID: `es.andaluh.pintarraheoStickers` (el mismo que en Android).
- Equipo: *AndaluGeeks Team*, Team ID `LAWPFLVDHJ`.
- Versión (`MARKETING_VERSION`): alineada a mano con `versionName` de `Android/app/build.gradle`.
- Número de build: `<run_number>.<run_attempt>` del workflow, siempre creciente.
- Stickers: copiados de `Android/app/src/main/assets/1/`; `sticker_packs.wasticker`
  generado a partir de `contents.json`. Si cambian los stickers en Android, hay que
  copiarlos aquí y actualizar `project.pbxproj` (grupo `SampleStickers`).
- Icono: el de alta resolución de la ficha de Google Play.
- Ficha de la tienda: textos copiados de Google Play; capturas hechas en un iPhone real.

## Cambios respecto al ejemplo de WhatsApp

- Sincronizado con los cambios iOS de `WhatsApp/stickers` hasta `06144a1` (accesibilidad,
  cabeceras de copyright). Android **no** está sincronizado con el upstream.
- Quitado el aviso "Don't ship this sample app!" del arranque.
- Interfaz en español; donde la app de Android tiene texto en andaluz EPA, se reutiliza.
- Corregido `stickerPack.publisher` en la cabecera del pack: con `stickerPack` como
  `StickerPack!`, en los SDK actuales ganaba `Optional.publisher` (Combine).

## Configuración en Apple (una sola vez, ya hecho)

- App ID registrado y app creada en App Store Connect (la API de Apple no permite crear apps).
- TestFlight: grupo de pruebas internas "AndaluGeeks" con distribución automática.

## Secretos de GitHub

Los mismos que en `andalugeeks/paisaje-linguistico` (API Key de equipo, rol Admin):
`ASC_KEY_P8`, `ASC_KEY_ID`, `ASC_ISSUER_ID`.

## Revisión del App Store

La 0.7 (build 7.1) se envió a revisión el 2026-11-16, con publicación automática. Apple suele
rechazar las apps que solo exportan stickers a WhatsApp (guideline 4.2, funcionalidad mínima);
si pasa, habrá que añadir funcionalidad propia según lo que diga la revisión.
