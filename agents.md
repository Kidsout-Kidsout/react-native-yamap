# LLM Guidelines for This Repo

This document is for LLMs (GitHub Copilot Agents, etc.) working in this repository. Follow these rules unless the user explicitly overrides them.

---

## 1. General Principles

- Prefer minimal, focused changes over large refactors.
- Preserve public APIs unless a change request clearly requires breaking them.
- Match existing code style and patterns in each platform / language.
- Keep code strongly typed (TypeScript, Kotlin, Objective‑C++ types) and avoid `any`/`id` unless unavoidable.
- When unsure about an external SDK detail (e.g., Yandex MapKit options), do not invent values; leave a `TODO` in code with a short explanation.

---

## 2. Working From Specs (JSI / TurboModule / View configs)

This library uses React Native New Architecture with codegen specs under `src/specs/` and native implementations in `ios/` and `android/`.

When creating or updating components from specs:

1. **Start from TS spec**
	- Prefer editing / adding TypeScript specs under `src/specs/` first.
	- Keep prop names and event names consistent between spec and platform code.
	- Use explicit types from `src/interfaces.ts` when possible instead of re‑declaring shapes.

2. **Regenerate bindings (conceptually)**
	- Assume JS/TS code is built by `react-native-builder-bob` and React Native codegen.
	- When changing specs, also:
	  - Export new components or modules from `src/index.ts` if they are part of the public API.
	  - Add or adjust TypeScript types if needed.

3. **Mirror in native code**
	- For every new or changed spec:
	  - **iOS:** implement / adjust in the matching `KDST*` Objective‑C++ class and view.
	  - **Android:** implement / adjust in the matching Kotlin view / module under `android/src/main/`.
	- Keep method signatures and prop/event names identical to the spec (case‑sensitive).

4. **Event naming**
	- JS side: `onSomethingHappened` props.
	- Native side: match the event key exactly in the exported interface.
	- Prefer concise payload objects with clear primitive types or small typed structs.

5. **Validation & errors**
	- Validate input parameters close to the boundary (JS module or native entry point).
	- Prefer throwing JS errors (or rejecting promises) with clear messages over silently ignoring invalid data.

---

## 3. iOS Coding Standards (Objective‑C++ / RN New Arch)

Native iOS code lives in `ios/`, mostly in files prefixed with `KDSTYamap*` and helpers in `Converter/`.

Follow these rules:

1. **Language & style**
	- Use Objective‑C++ (`.mm`) for implementation files when interacting with C++ MapKit APIs.
	- Use existing patterns from files like `KDSTYamapView.mm`, `KDSTYamapMarkerView.mm` as templates.
	- Stick to current naming conventions: `KDSTYamapXxx` for modules and views.

2. **React Native integration**
	- Keep new modules and components aligned with the existing codegen setup in `codegenConfig` from `package.json`.
	- When adding a new module/component:
	  - Ensure its `className` and spec name are registered in `codegenConfig.ios`.
	  - Implement required initializers, view lifecycle, and property setters following existing components.

3. **Memory & threading**
	- Avoid manual memory management; use ARC and strong/weak references consistently with existing code.
	- Ensure all MapKit/UI work happens on the main thread, especially view updates and delegate callbacks.

4. **Error handling**
	- Fail fast for clearly invalid arguments, but avoid crashing the app: prefer returning errors through promises or events.
	- When a native module is missing or not properly linked, surface clear error messages (similar to the `YamapConfig` invariant in TS).

5. **Interop helpers**
	- Reuse mapping / conversion helpers in `Converter/RCTConvert+Yamap.mm` where possible.
	- If you need new conversion logic, add it there and keep function names descriptive.

---

## 4. Android Coding Standards (Kotlin / RN New Arch)

Android code lives under `android/src/main/` (Kotlin, views, managers, modules).

Follow these rules:

1. **Language & style**
	- Use Kotlin for all new code.
	- Match the style of existing classes: package `ru.kidsout.yamap`, class names aligned with spec names.
	- Prefer data classes for small value objects where appropriate.

2. **React Native integration**
	- Use the New Architecture patterns already present in the codebase (turbo modules and Fabric components).
	- When adding a new component:
	  - Implement the View class.
	  - Implement the View Manager / Fabric component class.
	  - Wire props and events based on the generated spec interface.

3. **Threading & lifecycle**
	- Keep UI work on the main thread (`Looper.getMainLooper()`).
	- Clean up listeners and Yandex MapKit resources in `onDropViewInstance` / view destruction hooks.

4. **Error handling**
	- Use exceptions only for truly exceptional conditions; for invalid parameters, log and fail the specific call (e.g., reject a promise).
	- Avoid leaking native exceptions to JS without a clear message.

5. **Yandex MapKit usage**
	- Follow existing usage patterns in this repo when interacting with MapKit (e.g., camera moves, overlays).
	- Don’t introduce experimental or undocumented MapKit flags unless the user explicitly asks.

---

## 5. TypeScript / React Native Standards

Most JS/TS source lives in `src/` and is built to `lib/` by `react-native-builder-bob`.

1. **Imports & exports**
	- Export public APIs from `src/index.ts` only.
	- Keep component files small and focused (`src/components/*.tsx`).

2. **Types**
	- Use shared interfaces from `src/interfaces.ts` where possible.
	- Avoid `any`; prefer generics or explicit union types.

3. **Hooks & callbacks**
	- Prefer small hooks like `usePreventedCallback` and `CallbacksManager` to keep event logic DRY.
	- Debounce or guard against repeated native callbacks where needed.

4. **Error messages**
	- Use `invariant` from `src/utils/invariant.ts` for programmer errors (e.g., missing native module).
	- For runtime issues users may hit, surface user‑friendly messages and avoid crashing.

---

## 6. What Not to Do

- Do not change package name, Pod names, or public component names without an explicit user request.
- Do not invent Yandex MapKit APIs or constants; if unsure, leave a TODO.
- Do not introduce new third‑party dependencies without user approval.
- Do not reformat entire files; keep diffs minimal around the actual change.

---

## 7. When Ambiguous

- If a user request conflicts with these guidelines, follow the request but keep changes as safe and localized as possible.
- If a detail about native SDK behavior is unknown, clearly document the assumption in a comment or TODO instead of guessing implementation details.

