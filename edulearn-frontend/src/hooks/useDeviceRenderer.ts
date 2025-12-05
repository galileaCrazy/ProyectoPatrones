/**
 * HOOK: useDeviceRenderer
 *
 * Hook personalizado que implementa el patrón Bridge para renderizado adaptativo
 * Detecta automáticamente el tipo de dispositivo y proporciona configuraciones de estilo
 */

import { useEffect, useState, useMemo } from 'react';
import {
  TeachingInterfaceResponsive,
  createRendererForDevice,
  detectDevice,
  DetectedDeviceType,
  DeviceCapabilities,
  RenderedLayout,
  RenderedMedia,
  RenderedText
} from '@/lib/bridges/DeviceBridge';

interface DeviceRenderer {
  teachingInterface: TeachingInterfaceResponsive | null;
  deviceType: DetectedDeviceType;
  capabilities: DeviceCapabilities | null;
  isMobile: boolean;
  isTablet: boolean;
  isDesktop: boolean;
  isTV: boolean;
  renderLayout: (content: string) => RenderedLayout | null;
  renderMedia: (mediaUrl: string, type: 'video' | 'image') => RenderedMedia | null;
  renderText: (text: string, type: 'title' | 'paragraph' | 'list') => RenderedText | null;
  getResponsiveClasses: () => ResponsiveClasses;
}

interface ResponsiveClasses {
  container: string;
  grid: string;
  card: string;
  text: {
    title: string;
    subtitle: string;
    body: string;
  };
  spacing: {
    padding: string;
    margin: string;
    gap: string;
  };
  button: string;
  navigation: string;
}

export function useDeviceRenderer(): DeviceRenderer {
  const [teachingInterface, setTeachingInterface] = useState<TeachingInterfaceResponsive | null>(null);
  const [deviceType, setDeviceType] = useState<DetectedDeviceType>('web');
  const [capabilities, setCapabilities] = useState<DeviceCapabilities | null>(null);

  useEffect(() => {
    // Detectar dispositivo
    const detected = detectDevice();
    setDeviceType(detected);

    // Crear renderer apropiado
    const renderer = createRendererForDevice(detected);
    const ti = new TeachingInterfaceResponsive(renderer);

    setTeachingInterface(ti);
    setCapabilities(ti.getDeviceCapabilities());
  }, []);

  // Derivar información del tipo de dispositivo
  const isMobile = useMemo(() => {
    return deviceType === 'mobile' || (capabilities?.screenSize === 'small');
  }, [deviceType, capabilities]);

  const isTablet = useMemo(() => {
    return capabilities?.screenSize === 'medium';
  }, [capabilities]);

  const isDesktop = useMemo(() => {
    return deviceType === 'web' && !isMobile && !isTablet;
  }, [deviceType, isMobile, isTablet]);

  const isTV = useMemo(() => {
    return deviceType === 'tv' || capabilities?.screenSize === 'xlarge';
  }, [deviceType, capabilities]);

  // Funciones de renderizado
  const renderLayout = (content: string): RenderedLayout | null => {
    if (!teachingInterface) return null;
    const result = teachingInterface.renderContent(content);
    return result.layout;
  };

  const renderMedia = (mediaUrl: string, type: 'video' | 'image'): RenderedMedia | null => {
    if (!teachingInterface) return null;
    return teachingInterface.renderMaterial(mediaUrl, type);
  };

  const renderText = (text: string, type: 'title' | 'paragraph' | 'list'): RenderedText | null => {
    if (!teachingInterface) return null;
    return teachingInterface.renderTextContent(text, type);
  };

  // Obtener clases responsive basadas en el dispositivo
  const getResponsiveClasses = (): ResponsiveClasses => {
    if (isMobile) {
      return {
        container: 'mobile-container w-full px-3 py-4',
        grid: 'grid grid-cols-1 gap-3',
        card: 'rounded-lg p-4 shadow-sm',
        text: {
          title: 'text-xl font-bold',
          subtitle: 'text-base font-semibold',
          body: 'text-sm'
        },
        spacing: {
          padding: 'p-3',
          margin: 'm-3',
          gap: 'gap-3'
        },
        button: 'px-4 py-3 text-sm rounded-md min-h-[44px]', // Tamaño táctil mínimo
        navigation: 'fixed bottom-0 left-0 right-0 bg-white border-t shadow-lg'
      };
    }

    if (isTablet) {
      return {
        container: 'tablet-container w-full px-6 py-5',
        grid: 'grid grid-cols-2 gap-4',
        card: 'rounded-lg p-5 shadow-md',
        text: {
          title: 'text-2xl font-bold',
          subtitle: 'text-lg font-semibold',
          body: 'text-base'
        },
        spacing: {
          padding: 'p-4',
          margin: 'm-4',
          gap: 'gap-4'
        },
        button: 'px-5 py-3 text-base rounded-lg',
        navigation: 'fixed left-0 top-0 bottom-0 w-20 bg-white border-r shadow-md'
      };
    }

    if (isTV) {
      return {
        container: 'tv-container w-full px-12 py-10',
        grid: 'grid grid-cols-2 gap-8',
        card: 'rounded-xl p-8 shadow-xl',
        text: {
          title: 'text-5xl font-bold',
          subtitle: 'text-3xl font-semibold',
          body: 'text-2xl'
        },
        spacing: {
          padding: 'p-8',
          margin: 'm-8',
          gap: 'gap-8'
        },
        button: 'px-8 py-6 text-2xl rounded-xl',
        navigation: 'fixed left-0 top-0 bottom-0 w-32 bg-white border-r'
      };
    }

    // Desktop por defecto
    return {
      container: 'web-container max-w-7xl mx-auto px-8 py-6',
      grid: 'grid grid-cols-3 gap-6',
      card: 'rounded-lg p-6 shadow-md',
      text: {
        title: 'text-3xl font-bold',
        subtitle: 'text-xl font-semibold',
        body: 'text-base'
      },
      spacing: {
        padding: 'p-6',
        margin: 'm-6',
        gap: 'gap-6'
      },
      button: 'px-6 py-2.5 text-base rounded-lg',
      navigation: 'fixed left-0 top-0 bottom-0 w-64 bg-white border-r'
    };
  };

  return {
    teachingInterface,
    deviceType,
    capabilities,
    isMobile,
    isTablet,
    isDesktop,
    isTV,
    renderLayout,
    renderMedia,
    renderText,
    getResponsiveClasses
  };
}
