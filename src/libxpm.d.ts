declare module 'libxpm' {
  class XpmInfo {
    width: number;
    height: number;
    num_colors: number;
    cpp: number;
    color_map: {[color: string]: any};
    pixels: string;
  }

  export function render_to_canvas(xpm_info: any, c: any, options: any);
  export function parse_xpm(what: string): XpmInfo;
  export function xpm_to_img(what: string, options?: any): HTMLImageElement;
  export function get_color_value(what: any);
}
