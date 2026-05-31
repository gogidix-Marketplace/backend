import { useState, useCallback } from 'react';
import * as Papa from 'papaparse';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import { ExportOptions, ExportFormat } from '../types';

export function useExport() {
  const [isExporting, setIsExporting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const exportToCSV = useCallback((data: any[], filename: string) => {
    const csv = Papa.unparse(data);
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    link.setAttribute('href', url);
    link.setAttribute('download', `${filename}.csv`);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }, []);

  const exportToExcel = useCallback((data: any[], filename: string) => {
    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Data');

    XLSX.writeFile(workbook, `${filename}.xlsx`);
  }, []);

  const exportToJSON = useCallback((data: any, filename: string) => {
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], { type: 'application/json;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    link.setAttribute('href', url);
    link.setAttribute('download', `${filename}.json`);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }, []);

  const exportToPDF = useCallback((data: any[], filename: string, title?: string) => {
    const doc = new jsPDF();

    let yPos = 20;

    if (title) {
      doc.setFontSize(16);
      doc.text(title, 14, yPos);
      yPos += 10;
    }

    doc.setFontSize(10);

    data.forEach((row, index) => {
      if (yPos > 280) {
        doc.addPage();
        yPos = 20;
      }

      const text = typeof row === 'object' ? JSON.stringify(row) : String(row);
      const lines = doc.splitTextToSize(text, 180);

      lines.forEach((line: string) => {
        if (yPos > 280) {
          doc.addPage();
          yPos = 20;
        }
        doc.text(line, 14, yPos);
        yPos += 7;
      });
    });

    doc.save(`${filename}.pdf`);
  }, []);

  const exportData = useCallback(async (
    data: any[],
    options: ExportOptions
  ) => {
    setIsExporting(true);
    setError(null);

    try {
      const timestamp = new Date().toISOString().split('T')[0];
      const filename = `export-${timestamp}`;

      switch (options.format) {
        case 'csv':
          exportToCSV(data, filename);
          break;
        case 'excel':
          exportToExcel(data, filename);
          break;
        case 'pdf':
          exportToPDF(data, filename, 'Export Report');
          break;
        case 'json':
          exportToJSON(data, filename);
          break;
        default:
          throw new Error(`Unsupported export format: ${options.format}`);
      }

      return { success: true };
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Export failed';
      setError(message);
      return { success: false, error: message };
    } finally {
      setIsExporting(false);
    }
  }, [exportToCSV, exportToExcel, exportToPDF, exportToJSON]);

  return {
    exportData,
    isExporting,
    error,
    exportToCSV,
    exportToExcel,
    exportToPDF,
    exportToJSON,
  };
}

export function useFileUpload() {
  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const parseCSV = useCallback((file: File): Promise<any[]> => {
    return new Promise((resolve, reject) => {
      Papa.parse(file, {
        header: true,
        dynamicTyping: true,
        skipEmptyLines: true,
        complete: (results) => resolve(results.data),
        error: (error) => reject(error),
      });
    });
  }, []);

  const parseExcel = useCallback((file: File): Promise<any[]> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = (e) => {
        try {
          const data = new Uint8Array(e.target?.result as ArrayBuffer);
          const workbook = XLSX.read(data, { type: 'array' });
          const sheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[sheetName];
          const jsonData = XLSX.utils.sheet_to_json(worksheet);
          resolve(jsonData);
        } catch (err) {
          reject(err);
        }
      };

      reader.onerror = () => reject(new Error('Failed to read file'));
      reader.readAsArrayBuffer(file);
    });
  }, []);

  const uploadFile = useCallback(async (file: File): Promise<any[]> => {
    setIsUploading(true);
    setError(null);

    try {
      const extension = file.name.split('.').pop()?.toLowerCase();

      let data: any[];

      switch (extension) {
        case 'csv':
          data = await parseCSV(file);
          break;
        case 'xlsx':
        case 'xls':
          data = await parseExcel(file);
          break;
        default:
          throw new Error(`Unsupported file type: ${extension}`);
      }

      return data;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Upload failed';
      setError(message);
      throw err;
    } finally {
      setIsUploading(false);
    }
  }, [parseCSV, parseExcel]);

  return {
    uploadFile,
    isUploading,
    error,
  };
}
