# Patrón Bridge - Guía de Implementación en EduLearn

## 📋 Descripción

El patrón **Bridge** (Puente) es un patrón estructural que desacopla una abstracción de su implementación, permitiendo que ambas varíen independientemente. En EduLearn, lo utilizamos para separar la **interfaz de enseñanza** (qué mostrar) de la **renderización por dispositivo** (cómo mostrarlo).

## 🎯 Problema que Resuelve

Sin el patrón Bridge, cada vez que agregamos:
- Un nuevo tipo de contenido educativo (video, quiz, material)
- Un nuevo dispositivo (tablet, consola, VR)

Tendríamos que crear combinaciones como: `VideoWeb`, `VideoMobile`, `VideoTV`, `QuizWeb`, `QuizMobile`, etc., generando una explosión de clases.

## ✅ Solución con Bridge

El patrón Bridge separa:

```
TeachingInterface (ABSTRACCIÓN) ←→ DeviceRenderer (IMPLEMENTACIÓN)
       ↓                                    ↓
TeachingInterfaceResponsive          Web / Mobile / TV
```

## 🏗️ Estructura del Patrón

### 1. Implementor Interface (DeviceRenderer)

Define los métodos que cada dispositivo debe implementar:

```typescript
export interface DeviceRenderer {
  renderLayout(content: string): RenderedLayout;
  renderMedia(mediaUrl: string, mediaType: 'video' | 'image'): RenderedMedia;
  renderText(text: string, textType: 'title' | 'paragraph' | 'list'): RenderedText;
  getCapabilities(): DeviceCapabilities;
  getDeviceName(): string;
}
```

### 2. Concrete Implementors

Implementaciones específicas para cada dispositivo:

- **WebRenderer**: Renderización para navegadores desktop
- **MobileRenderer**: Renderización optimizada para móviles
- **SmartTVRenderer**: Renderización para televisores inteligentes

### 3. Abstraction (TeachingInterface)

Clase abstracta que define operaciones de alto nivel:

```typescript
export class TeachingInterface {
  protected renderer: DeviceRenderer;

  constructor(renderer: DeviceRenderer) {
    this.renderer = renderer;
  }

  renderContent(content: string) { ... }
  renderVideo(videoUrl: string) { ... }
  renderMaterial(materialUrl: string, type: 'image') { ... }
}
```

### 4. Refined Abstraction (TeachingInterfaceResponsive)

Extiende la abstracción con funcionalidad responsive:

```typescript
export class TeachingInterfaceResponsive extends TeachingInterface {
  renderAdaptiveContent(content: string) { ... }
  renderAdaptiveVideo(videoUrl: string) { ... }
  renderLesson(lesson: LessonData) { ... }
}
```

## 💻 Ejemplos de Uso

### Ejemplo 1: Uso Básico

```typescript
import {
  WebRenderer,
  MobileRenderer,
  TeachingInterface
} from '@/lib/bridges/DeviceBridge';

// Crear renderer para web
const webRenderer = new WebRenderer();
const teachingInterface = new TeachingInterface(webRenderer);

// Renderizar contenido
const content = teachingInterface.renderContent("Contenido del curso");
console.log(content);
// { layout: { containerClass: 'web-container max-w-7xl mx-auto', ... }, ... }

// Renderizar video
const video = teachingInterface.renderVideo("https://example.com/video.mp4");
console.log(video);
// { containerClass: 'web-media-container rounded-lg shadow-lg', width: '100%', ... }
```

### Ejemplo 2: Cambio Dinámico de Dispositivo

```typescript
import {
  TeachingInterface,
  WebRenderer,
  MobileRenderer
} from '@/lib/bridges/DeviceBridge';

const teachingInterface = new TeachingInterface(new WebRenderer());

// Renderizar para web
const webContent = teachingInterface.renderContent("Lección 1");
console.log(webContent.deviceInfo); // "Renderizado para: Web Desktop"

// Cambiar a móvil dinámicamente
teachingInterface.setRenderer(new MobileRenderer());

const mobileContent = teachingInterface.renderContent("Lección 1");
console.log(mobileContent.deviceInfo); // "Renderizado para: Mobile"
```

### Ejemplo 3: Uso con Detección Automática

```typescript
import { createTeachingInterface } from '@/lib/bridges/DeviceBridge';

// Detección automática del dispositivo
const teachingInterface = createTeachingInterface(true); // true = responsive

// El sistema detecta automáticamente si es web, móvil o TV
const content = teachingInterface.renderAdaptiveContent("Contenido");
console.log(content.adaptations);
// ['Botones más grandes para touch'] si está en móvil
// ['Layout de una columna'] si está en móvil
```

### Ejemplo 4: Renderizar Lección Completa

```typescript
import { TeachingInterfaceResponsive, WebRenderer } from '@/lib/bridges/DeviceBridge';

const teachingInterface = new TeachingInterfaceResponsive(new WebRenderer());

const lesson = {
  title: "Introducción a TypeScript",
  description: "Aprende los fundamentos de TypeScript",
  videoUrl: "https://example.com/typescript-intro.mp4",
  materialUrl: "https://example.com/slides.png",
  content: [
    "TypeScript es un superset de JavaScript",
    "Agrega tipado estático al código",
    "Mejora la mantenibilidad del proyecto"
  ]
};

const result = teachingInterface.renderLesson(lesson);

console.log(result.deviceName); // "Web Desktop"
console.log(result.isFullyInteractive); // true
console.log(result.recommendedLayout); // "multi-column-responsive"
console.log(result.video?.qualityReason); // "Alta calidad - dispositivo soporta HD"
```

### Ejemplo 5: Verificar Capacidades del Dispositivo

```typescript
import { createTeachingInterface } from '@/lib/bridges/DeviceBridge';

const teachingInterface = createTeachingInterface(true);

// Verificar si el dispositivo puede manejar contenido interactivo
if (teachingInterface.canHandleContent('interactive')) {
  console.log("Mostrar quiz interactivo");
} else {
  console.log("Mostrar quiz estático");
}

// Verificar si tiene scroll
if (teachingInterface.canHandleContent('scroll')) {
  console.log("Mostrar contenido largo");
} else {
  console.log("Paginar contenido");
}

// Obtener todas las capacidades
const capabilities = teachingInterface.getDeviceCapabilities();
console.log(capabilities);
// {
//   hasTouch: false,
//   hasKeyboard: true,
//   hasScroll: true,
//   supportsHD: true,
//   supportsInteractivity: true,
//   screenSize: 'large'
// }
```

## 🔧 Integración con Componentes React

### Ejemplo: Hook Personalizado

```typescript
// hooks/useDeviceRenderer.ts
import { useEffect, useState } from 'react';
import {
  TeachingInterfaceResponsive,
  createRendererForDevice,
  DetectedDeviceType
} from '@/lib/bridges/DeviceBridge';

export function useDeviceRenderer() {
  const [teachingInterface, setTeachingInterface] = useState<TeachingInterfaceResponsive | null>(null);

  useEffect(() => {
    const renderer = createRendererForDevice();
    const ti = new TeachingInterfaceResponsive(renderer);
    setTeachingInterface(ti);
  }, []);

  return teachingInterface;
}
```

### Ejemplo: Componente de Lección

```typescript
// components/LessonViewer.tsx
'use client';

import { useDeviceRenderer } from '@/hooks/useDeviceRenderer';
import { useEffect, useState } from 'react';

interface LessonViewerProps {
  lesson: {
    title: string;
    description: string;
    videoUrl?: string;
    content: string[];
  };
}

export function LessonViewer({ lesson }: LessonViewerProps) {
  const teachingInterface = useDeviceRenderer();
  const [renderData, setRenderData] = useState<any>(null);

  useEffect(() => {
    if (teachingInterface) {
      const data = teachingInterface.renderLesson(lesson);
      setRenderData(data);
    }
  }, [teachingInterface, lesson]);

  if (!renderData) {
    return <div>Cargando...</div>;
  }

  return (
    <div className={renderData.layout.containerClass}>
      <h1
        style={{
          fontSize: renderData.title.fontSize,
          lineHeight: renderData.title.lineHeight
        }}
        className={renderData.title.containerClass}
      >
        {lesson.title}
      </h1>

      <p
        style={{
          fontSize: renderData.description.fontSize,
          lineHeight: renderData.description.lineHeight
        }}
        className={renderData.description.containerClass}
      >
        {lesson.description}
      </p>

      {renderData.video && (
        <div>
          <video
            src={lesson.videoUrl}
            className={renderData.video.media.containerClass}
            style={{
              width: renderData.video.media.width,
              aspectRatio: renderData.video.media.aspectRatio
            }}
            controls={renderData.video.media.controls}
            autoPlay={renderData.video.media.autoplay}
          />
          <p className="text-sm text-gray-500">
            {renderData.video.qualityReason}
          </p>
        </div>
      )}

      {renderData.content.map((contentStyle: any, index: number) => (
        <p
          key={index}
          style={{
            fontSize: contentStyle.fontSize,
            lineHeight: contentStyle.lineHeight
          }}
          className={contentStyle.containerClass}
        >
          {lesson.content[index]}
        </p>
      ))}

      <div className="mt-4 text-sm text-gray-600">
        <p>Dispositivo: {renderData.deviceName}</p>
        <p>Layout recomendado: {renderData.recommendedLayout}</p>
        {!renderData.isFullyInteractive && (
          <p className="text-amber-600">
            ⚠️ Interactividad limitada en este dispositivo
          </p>
        )}
      </div>
    </div>
  );
}
```

## 🎨 Integración con Sistema Responsive Existente

El patrón Bridge trabaja **junto con** tu sistema responsive, no lo reemplaza:

```typescript
// El responsive CSS maneja los breakpoints visuales:
// @media (max-width: 768px) { ... }

// El Bridge maneja la lógica de dispositivo:
const capabilities = teachingInterface.getDeviceCapabilities();

if (capabilities.screenSize === 'small') {
  // Lógica específica para pantallas pequeñas
} else if (!capabilities.hasScroll) {
  // Lógica específica para dispositivos sin scroll (TV)
}
```

## 🚀 Ventajas del Patrón

1. **Extensibilidad**: Agregar nuevos dispositivos sin modificar código existente
2. **Flexibilidad**: Cambiar el renderer en tiempo de ejecución
3. **Mantenibilidad**: Separación clara entre abstracción e implementación
4. **Reutilización**: Los renderers pueden usarse con diferentes abstracciones
5. **Testabilidad**: Fácil crear mocks de renderers para pruebas

## 📦 Agregando Nuevos Dispositivos

Para agregar un nuevo dispositivo (ej: Tablet):

```typescript
export class TabletRenderer implements DeviceRenderer {
  renderLayout(content: string): RenderedLayout {
    return {
      containerClass: 'tablet-container',
      maxWidth: '1024px',
      padding: '1.5rem',
      gridColumns: 2,
      gap: '1.25rem',
      scrollable: true
    };
  }

  renderMedia(mediaUrl: string, mediaType: 'video' | 'image'): RenderedMedia {
    return {
      containerClass: 'tablet-media-container',
      width: '100%',
      height: 'auto',
      aspectRatio: '16/9',
      autoplay: false,
      controls: true,
      quality: 'high'
    };
  }

  renderText(text: string, textType: 'title' | 'paragraph' | 'list'): RenderedText {
    return {
      containerClass: `tablet-text-${textType}`,
      fontSize: '1.1rem',
      lineHeight: '1.5',
      maxWidth: '70ch',
      textAlign: 'left'
    };
  }

  getCapabilities(): DeviceCapabilities {
    return {
      hasTouch: true,
      hasKeyboard: false,
      hasScroll: true,
      supportsHD: true,
      supportsInteractivity: true,
      screenSize: 'medium'
    };
  }

  getDeviceName(): string {
    return 'Tablet';
  }
}
```

## 🧪 Testing

```typescript
import { TeachingInterface, WebRenderer, MobileRenderer } from '@/lib/bridges/DeviceBridge';

describe('Bridge Pattern - TeachingInterface', () => {
  it('should render content for web device', () => {
    const ti = new TeachingInterface(new WebRenderer());
    const result = ti.renderContent("Test content");

    expect(result.layout.containerClass).toContain('web-container');
    expect(result.deviceInfo).toBe('Renderizado para: Web Desktop');
  });

  it('should render content for mobile device', () => {
    const ti = new TeachingInterface(new MobileRenderer());
    const result = ti.renderContent("Test content");

    expect(result.layout.containerClass).toContain('mobile-container');
    expect(result.deviceInfo).toBe('Renderizado para: Mobile');
  });

  it('should change renderer dynamically', () => {
    const ti = new TeachingInterface(new WebRenderer());
    expect(ti.getDeviceCapabilities().hasTouch).toBe(false);

    ti.setRenderer(new MobileRenderer());
    expect(ti.getDeviceCapabilities().hasTouch).toBe(true);
  });
});
```

## 📚 Referencias

- **Libro**: "Design Patterns: Elements of Reusable Object-Oriented Software" (Gang of Four)
- **Tipo**: Patrón Estructural
- **Propósito**: Desacoplar abstracción de implementación
- **También conocido como**: Handle/Body

## 🔗 Relación con otros Patrones

- **Adapter**: Bridge se diseña por adelantado; Adapter se usa para hacer que clases incompatibles trabajen juntas
- **Strategy**: Bridge separa abstracción de implementación; Strategy separa algoritmos
- **Abstract Factory**: Puede usarse para crear renderers específicos
