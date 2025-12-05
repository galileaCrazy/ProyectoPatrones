/**
 * PATRÓN ESTRUCTURAL: BRIDGE
 *
 * Propósito: Desacoplar la abstracción de enseñanza de su implementación de renderización,
 * permitiendo que ambas varíen independientemente según el dispositivo.
 *
 * Estructura:
 * - Implementor (DeviceRenderer): Define la interfaz para las implementaciones concretas
 * - Concrete Implementors: WebRenderer, MobileRenderer, SmartTVRenderer
 * - Abstraction (TeachingInterface): Define la interfaz de alto nivel
 * - Refined Abstraction (TeachingInterfaceResponsive): Extiende con funcionalidad responsive
 */

// ============================================================================
// IMPLEMENTOR: Define la interfaz para todas las implementaciones de dispositivos
// ============================================================================

/**
 * Interface que define los métodos que cada dispositivo debe implementar.
 * Esta es la parte "implementación" del Bridge.
 */
export interface DeviceRenderer {
  /**
   * Renderiza el layout general del contenido educativo
   */
  renderLayout(content: string): RenderedLayout;

  /**
   * Renderiza elementos multimedia (videos, imágenes)
   */
  renderMedia(mediaUrl: string, mediaType: 'video' | 'image'): RenderedMedia;

  /**
   * Renderiza contenido de texto
   */
  renderText(text: string, textType: 'title' | 'paragraph' | 'list'): RenderedText;

  /**
   * Obtiene información sobre las capacidades del dispositivo
   */
  getCapabilities(): DeviceCapabilities;

  /**
   * Obtiene el nombre del dispositivo
   */
  getDeviceName(): string;
}

// ============================================================================
// TIPOS DE DATOS
// ============================================================================

export interface RenderedLayout {
  containerClass: string;
  maxWidth: string;
  padding: string;
  gridColumns: number;
  gap: string;
  scrollable: boolean;
}

export interface RenderedMedia {
  containerClass: string;
  width: string;
  height: string;
  aspectRatio: string;
  autoplay: boolean;
  controls: boolean;
  quality: 'high' | 'medium' | 'low';
}

export interface RenderedText {
  containerClass: string;
  fontSize: string;
  lineHeight: string;
  maxWidth: string;
  textAlign: 'left' | 'center' | 'right';
}

export interface DeviceCapabilities {
  hasTouch: boolean;
  hasKeyboard: boolean;
  hasScroll: boolean;
  supportsHD: boolean;
  supportsInteractivity: boolean;
  screenSize: 'small' | 'medium' | 'large' | 'xlarge';
}

// ============================================================================
// CONCRETE IMPLEMENTORS
// ============================================================================

/**
 * Implementación concreta para dispositivos Web (Desktop)
 */
export class WebRenderer implements DeviceRenderer {
  renderLayout(content: string): RenderedLayout {
    return {
      containerClass: 'web-container max-w-7xl mx-auto',
      maxWidth: '1280px',
      padding: '2rem',
      gridColumns: 3,
      gap: '1.5rem',
      scrollable: true
    };
  }

  renderMedia(mediaUrl: string, mediaType: 'video' | 'image'): RenderedMedia {
    return {
      containerClass: 'web-media-container rounded-lg shadow-lg',
      width: '100%',
      height: 'auto',
      aspectRatio: '16/9',
      autoplay: false,
      controls: true,
      quality: 'high'
    };
  }

  renderText(text: string, textType: 'title' | 'paragraph' | 'list'): RenderedText {
    const textStyles: Record<typeof textType, Partial<RenderedText>> = {
      title: { fontSize: '2.5rem', lineHeight: '1.2' },
      paragraph: { fontSize: '1rem', lineHeight: '1.6' },
      list: { fontSize: '0.95rem', lineHeight: '1.5' }
    };

    return {
      containerClass: `web-text-${textType}`,
      fontSize: textStyles[textType].fontSize || '1rem',
      lineHeight: textStyles[textType].lineHeight || '1.5',
      maxWidth: '65ch',
      textAlign: 'left'
    };
  }

  getCapabilities(): DeviceCapabilities {
    return {
      hasTouch: false,
      hasKeyboard: true,
      hasScroll: true,
      supportsHD: true,
      supportsInteractivity: true,
      screenSize: 'large'
    };
  }

  getDeviceName(): string {
    return 'Web Desktop';
  }
}

/**
 * Implementación concreta para dispositivos Móviles
 */
export class MobileRenderer implements DeviceRenderer {
  renderLayout(content: string): RenderedLayout {
    return {
      containerClass: 'mobile-container w-full',
      maxWidth: '100%',
      padding: '1rem',
      gridColumns: 1,
      gap: '1rem',
      scrollable: true
    };
  }

  renderMedia(mediaUrl: string, mediaType: 'video' | 'image'): RenderedMedia {
    return {
      containerClass: 'mobile-media-container rounded-md',
      width: '100%',
      height: 'auto',
      aspectRatio: '16/9',
      autoplay: false,
      controls: true,
      quality: 'medium'
    };
  }

  renderText(text: string, textType: 'title' | 'paragraph' | 'list'): RenderedText {
    const textStyles: Record<typeof textType, Partial<RenderedText>> = {
      title: { fontSize: '1.75rem', lineHeight: '1.3' },
      paragraph: { fontSize: '0.95rem', lineHeight: '1.6' },
      list: { fontSize: '0.9rem', lineHeight: '1.5' }
    };

    return {
      containerClass: `mobile-text-${textType}`,
      fontSize: textStyles[textType].fontSize || '0.95rem',
      lineHeight: textStyles[textType].lineHeight || '1.5',
      maxWidth: '100%',
      textAlign: 'left'
    };
  }

  getCapabilities(): DeviceCapabilities {
    return {
      hasTouch: true,
      hasKeyboard: false,
      hasScroll: true,
      supportsHD: false,
      supportsInteractivity: true,
      screenSize: 'small'
    };
  }

  getDeviceName(): string {
    return 'Mobile';
  }
}

/**
 * Implementación concreta para Smart TV
 */
export class SmartTVRenderer implements DeviceRenderer {
  renderLayout(content: string): RenderedLayout {
    return {
      containerClass: 'tv-container w-full h-screen',
      maxWidth: '100%',
      padding: '3rem',
      gridColumns: 2,
      gap: '2rem',
      scrollable: false // Las Smart TVs típicamente no tienen scroll
    };
  }

  renderMedia(mediaUrl: string, mediaType: 'video' | 'image'): RenderedMedia {
    return {
      containerClass: 'tv-media-container',
      width: '100%',
      height: '100%',
      aspectRatio: '16/9',
      autoplay: true,
      controls: true,
      quality: 'high' // Las TVs soportan alta calidad
    };
  }

  renderText(text: string, textType: 'title' | 'paragraph' | 'list'): RenderedText {
    const textStyles: Record<typeof textType, Partial<RenderedText>> = {
      title: { fontSize: '3.5rem', lineHeight: '1.2' },
      paragraph: { fontSize: '1.5rem', lineHeight: '1.6' },
      list: { fontSize: '1.25rem', lineHeight: '1.5' }
    };

    return {
      containerClass: `tv-text-${textType}`,
      fontSize: textStyles[textType].fontSize || '1.5rem',
      lineHeight: textStyles[textType].lineHeight || '1.5',
      maxWidth: '80ch',
      textAlign: 'center' // En TV el texto suele estar centrado
    };
  }

  getCapabilities(): DeviceCapabilities {
    return {
      hasTouch: false,
      hasKeyboard: false,
      hasScroll: false,
      supportsHD: true,
      supportsInteractivity: false, // Interactividad limitada en TV
      screenSize: 'xlarge'
    };
  }

  getDeviceName(): string {
    return 'Smart TV';
  }
}

// ============================================================================
// ABSTRACTION: Define la interfaz de alto nivel
// ============================================================================

/**
 * Abstracción que define las operaciones de enseñanza.
 * Mantiene una referencia al implementador (DeviceRenderer).
 */
export class TeachingInterface {
  protected renderer: DeviceRenderer;

  constructor(renderer: DeviceRenderer) {
    this.renderer = renderer;
  }

  /**
   * Renderiza el contenido educativo completo
   */
  renderContent(content: string): {
    layout: RenderedLayout;
    deviceInfo: string;
  } {
    const layout = this.renderer.renderLayout(content);
    return {
      layout,
      deviceInfo: `Renderizado para: ${this.renderer.getDeviceName()}`
    };
  }

  /**
   * Renderiza un video educativo
   */
  renderVideo(videoUrl: string): RenderedMedia {
    return this.renderer.renderMedia(videoUrl, 'video');
  }

  /**
   * Renderiza material educativo (imágenes, PDFs, etc.)
   */
  renderMaterial(materialUrl: string, type: 'image'): RenderedMedia {
    return this.renderer.renderMedia(materialUrl, type);
  }

  /**
   * Renderiza texto con formato
   */
  renderTextContent(text: string, type: 'title' | 'paragraph' | 'list'): RenderedText {
    return this.renderer.renderText(text, type);
  }

  /**
   * Obtiene las capacidades del dispositivo actual
   */
  getDeviceCapabilities(): DeviceCapabilities {
    return this.renderer.getCapabilities();
  }

  /**
   * Cambia el renderer en tiempo de ejecución (flexibilidad del Bridge)
   */
  setRenderer(renderer: DeviceRenderer): void {
    this.renderer = renderer;
  }
}

// ============================================================================
// REFINED ABSTRACTION: Extiende la abstracción con funcionalidad responsive
// ============================================================================

/**
 * Abstracción refinada que agrega funcionalidad de diseño responsive.
 * Usa las capacidades del dispositivo para ajustar automáticamente el contenido.
 */
export class TeachingInterfaceResponsive extends TeachingInterface {
  /**
   * Renderiza contenido adaptándose automáticamente a las capacidades del dispositivo
   */
  renderAdaptiveContent(content: string): {
    layout: RenderedLayout;
    deviceInfo: string;
    adaptations: string[];
  } {
    const capabilities = this.renderer.getCapabilities();
    const baseRender = this.renderContent(content);
    const adaptations: string[] = [];

    // Adaptar basado en capacidades
    if (!capabilities.hasScroll) {
      adaptations.push('Contenido paginado (sin scroll)');
    }

    if (capabilities.hasTouch) {
      adaptations.push('Botones más grandes para touch');
    }

    if (!capabilities.supportsInteractivity) {
      adaptations.push('Navegación simplificada');
    }

    if (capabilities.screenSize === 'small') {
      adaptations.push('Layout de una columna');
    } else if (capabilities.screenSize === 'xlarge') {
      adaptations.push('Textos más grandes para lectura a distancia');
    }

    return {
      ...baseRender,
      adaptations
    };
  }

  /**
   * Renderiza video con ajustes automáticos de calidad
   */
  renderAdaptiveVideo(videoUrl: string): {
    media: RenderedMedia;
    qualityReason: string;
  } {
    const capabilities = this.renderer.getCapabilities();
    const media = this.renderVideo(videoUrl);

    let qualityReason = '';
    if (capabilities.supportsHD) {
      qualityReason = 'Alta calidad - dispositivo soporta HD';
    } else {
      qualityReason = 'Calidad media - optimizado para ancho de banda móvil';
    }

    return { media, qualityReason };
  }

  /**
   * Renderiza una lección completa con todos sus elementos
   */
  renderLesson(lesson: {
    title: string;
    description: string;
    videoUrl?: string;
    materialUrl?: string;
    content: string[];
  }): LessonRenderResult {
    const capabilities = this.renderer.getCapabilities();

    const titleStyle = this.renderTextContent(lesson.title, 'title');
    const descriptionStyle = this.renderTextContent(lesson.description, 'paragraph');

    const contentStyles = lesson.content.map(item =>
      this.renderTextContent(item, 'paragraph')
    );

    const videoStyle = lesson.videoUrl
      ? this.renderAdaptiveVideo(lesson.videoUrl)
      : null;

    const materialStyle = lesson.materialUrl
      ? this.renderMaterial(lesson.materialUrl, 'image')
      : null;

    const layout = this.renderer.renderLayout(lesson.content.join('\n'));

    return {
      title: titleStyle,
      description: descriptionStyle,
      content: contentStyles,
      video: videoStyle,
      material: materialStyle,
      layout,
      deviceName: this.renderer.getDeviceName(),
      capabilities,
      isFullyInteractive: capabilities.supportsInteractivity,
      recommendedLayout: this.getRecommendedLayout(capabilities)
    };
  }

  /**
   * Determina el layout recomendado basado en las capacidades del dispositivo
   */
  private getRecommendedLayout(capabilities: DeviceCapabilities): string {
    if (capabilities.screenSize === 'small') {
      return 'single-column-mobile';
    } else if (capabilities.screenSize === 'xlarge' && !capabilities.hasScroll) {
      return 'paginated-tv';
    } else {
      return 'multi-column-responsive';
    }
  }

  /**
   * Verifica si el dispositivo puede manejar un tipo específico de contenido
   */
  canHandleContent(contentType: 'interactive' | 'video' | 'text' | 'scroll'): boolean {
    const capabilities = this.renderer.getCapabilities();

    switch (contentType) {
      case 'interactive':
        return capabilities.supportsInteractivity;
      case 'video':
        return true; // Todos los dispositivos soportan video
      case 'text':
        return true; // Todos los dispositivos soportan texto
      case 'scroll':
        return capabilities.hasScroll;
      default:
        return false;
    }
  }
}

// ============================================================================
// TIPOS AUXILIARES
// ============================================================================

export interface LessonRenderResult {
  title: RenderedText;
  description: RenderedText;
  content: RenderedText[];
  video: { media: RenderedMedia; qualityReason: string } | null;
  material: RenderedMedia | null;
  layout: RenderedLayout;
  deviceName: string;
  capabilities: DeviceCapabilities;
  isFullyInteractive: boolean;
  recommendedLayout: string;
}

// ============================================================================
// UTILIDAD: Detección de dispositivo
// ============================================================================

/**
 * Tipo de dispositivo detectado
 */
export type DetectedDeviceType = 'web' | 'mobile' | 'tv';

/**
 * Detecta el tipo de dispositivo basándose en el User Agent y características del navegador
 */
export function detectDevice(): DetectedDeviceType {
  if (typeof window === 'undefined') {
    return 'web'; // Por defecto en SSR
  }

  const userAgent = navigator.userAgent.toLowerCase();
  const width = window.innerWidth;

  // Detectar TV
  if (
    userAgent.includes('smart-tv') ||
    userAgent.includes('smarttv') ||
    userAgent.includes('googletv') ||
    userAgent.includes('appletv') ||
    width >= 1920
  ) {
    return 'tv';
  }

  // Detectar móvil
  if (
    /android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(userAgent) ||
    width <= 768
  ) {
    return 'mobile';
  }

  // Por defecto web
  return 'web';
}

/**
 * Crea un renderer apropiado basándose en el dispositivo detectado
 */
export function createRendererForDevice(deviceType?: DetectedDeviceType): DeviceRenderer {
  const device = deviceType || detectDevice();

  switch (device) {
    case 'mobile':
      return new MobileRenderer();
    case 'tv':
      return new SmartTVRenderer();
    case 'web':
    default:
      return new WebRenderer();
  }
}

/**
 * Factory para crear una interfaz de enseñanza con detección automática de dispositivo
 */
export function createTeachingInterface(
  responsive: boolean = true,
  deviceType?: DetectedDeviceType
): TeachingInterface | TeachingInterfaceResponsive {
  const renderer = createRendererForDevice(deviceType);

  if (responsive) {
    return new TeachingInterfaceResponsive(renderer);
  }

  return new TeachingInterface(renderer);
}

// ============================================================================
// EXPORTACIONES
// ============================================================================

export default {
  TeachingInterface,
  TeachingInterfaceResponsive,
  WebRenderer,
  MobileRenderer,
  SmartTVRenderer,
  detectDevice,
  createRendererForDevice,
  createTeachingInterface
};
